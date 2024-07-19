package com.xkcoding.excel.multiwrite;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.write.handler.WriteHandler;
import com.apifan.common.random.util.JsonUtils;
import com.xkcoding.common.utils.DateUtils;
import com.xkcoding.excel.multiwrite.dto.*;
import com.xkcoding.excel.multiwrite.enums.ImportFileSourceEnum;
import com.xkcoding.excel.multiwrite.enums.ImportFileTemplateEnum;
import com.xkcoding.excel.multiwrite.enums.ImportStatus;
import com.xkcoding.excel.multiwrite.write.EasyExcelWriteUtils;
import com.xkcoding.excel.multiwrite.write.ExcelSheetWriteContextVO;
import com.xkcoding.excel.multiwrite.write.ExcelWriteTableVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 功能描述: <br>
 * <导入文件处理模板>
 * T 数据库对象
 *
 * @since: 1.0.0
 * @Author: XuGang
 * @Date: 2024/3/12 18:42
 */
@Slf4j
public abstract class AbstractImportFileTemplate {

    /**
     * 最大数据数量（1W条）
     */
    private static final int MAX_DATA_COUNT = 10_000;

    /**
     * 批量插入子表数量
     */
    protected static final int DEFAULT_BATCH_COUNT = 20;

    /**
     * 导入最大数据量格式
     */
    private static final String IMPORT_MAX_DATA_ROWS_FORMAT = "一次导入最多%d条数据！";

    /**
     * 校验匹配，用于handle获取具体实现
     **/
    public Boolean checkMatched(ImportFileMetaDTO metaDTO) {
        return getTemplateEnum().equals(metaDTO.getImportFileTemplateEnum());
    }

    public abstract ImportFileTemplateEnum getTemplateEnum();

    public ImportFileResultDTO doImportFile(ImportFileMetaDTO importFileMetaDTO) {
        ImportFileResultDTO importFileResultDTO = getImportFileByteArray(importFileMetaDTO);
        if (ImportStatus.FAIL.equals(importFileResultDTO.getImportStatus())) {
            return importFileResultDTO;
        }
        byte[] byteArray = importFileResultDTO.getByteArray();
        //# 解析文件
        List<ExcelSheetReadContext> sheetReadContexts = new ArrayList<>();
        try {
            log.info("获取导入Excel文件流成功，开始解析文件！");
            sheetReadContexts = configExcelSheetReadContext(byteArray);
            parsingFile(sheetReadContexts);
            log.info("解析文件成功！");
        } catch (Exception e) {
            log.error("解析读取文件失败！入参：{}", JsonUtils.toJson(importFileMetaDTO), e);
            return this.returnResult(ImportStatus.FAIL, e.getMessage());
        }
        List<ExcelReadTableVO> allReadTableVOS = sheetReadContexts.stream()
                                                                  .map(ExcelSheetReadContext::getExcelReadTables)
                                                                  .flatMap(Collection::stream)
                                                                  .collect(Collectors.toList());
        //## 获取统计结果
        ExcelReadStatisticsResultDTO statisticsResult = getStatisticsResult(allReadTableVOS);
        //## 数据量校验
        Optional<ImportFileResultDTO> importFileResultDTOOptional = checkDataCount(statisticsResult, importFileMetaDTO);
        if (importFileResultDTOOptional.isPresent()) {
            return importFileResultDTOOptional.get();
        }
        String importFileDetail = statisticsResult.getImportFileDetail();
        if (statisticsResult.hasValidateError()) {
            log.warn("导入Excel存在校验错误，准备上传到OBS！结果简略：{}", importFileDetail);
            ExportErrorFileResultDTO errorFileInfo = exportErrorFile(sheetReadContexts,
                                                                     importFileMetaDTO.getFileName());
            log.warn("错误文件上传到OBS完成！文件名：{}，文件Key：{}", errorFileInfo.getName(), errorFileInfo.getKey());
            // 返回失败
            return this.returnResult(ImportStatus.FAIL,
                                     importFileDetail,
                                     errorFileInfo.getName(),
                                     errorFileInfo.getKey());
        }
        //## 处理数据
        handleDataList(allReadTableVOS, statisticsResult, importFileMetaDTO);
        log.info("导入成功，结果简略：{}", importFileDetail);
        return this.returnResult(ImportStatus.SUCCESS, importFileDetail);
    }

    /**
     * 获取导入文件字节数组
     *
     * @param importFileMetaDTO
     * @return
     */
    private ImportFileResultDTO getImportFileByteArray(ImportFileMetaDTO importFileMetaDTO) {
        switch (importFileMetaDTO.getImportFileSourceEnum()) {
            case OBS:
                return getImportFileByteArrayFromObs(importFileMetaDTO);
            case LOCAL:
                return getImportFileByteArrayFromLocalFile(importFileMetaDTO);
            default:
                return this.returnResult(ImportStatus.FAIL, "获取导入文件字节数组失败！");
        }
    }

    private ImportFileResultDTO getImportFileByteArrayFromObs(ImportFileMetaDTO importFileMetaDTO) {
        byte[] byteArray = new byte[0];
        //# 校验文件是否存在
        if (!ObsFileService.doesObjectExist(importFileMetaDTO.getFilePath())) {
            return this.returnResult("文件不存在!", importFileMetaDTO);
        }
        //# 获取文件流，并转换为字节数组，用于反复读取
        log.info("开始从Obs获取导入Excel文件流！文件路径：{}", importFileMetaDTO.getFilePath());
        try (InputStream inputStream = ObsFileService.getObject(importFileMetaDTO.getFilePath())) {
            byteArray = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            log.error("从Obs获取导入Excel文件流失败！入参：{}", importFileMetaDTO, e);
            return this.returnResult(ImportStatus.FAIL, e.getMessage());
        }
        return successGetImportFileByteArray(byteArray);
    }

    private ImportFileResultDTO getImportFileByteArrayFromLocalFile(ImportFileMetaDTO importFileMetaDTO) {
        byte[] byteArray = new byte[0];
        //# 校验文件是否存在
        if (!FileUtil.exist(importFileMetaDTO.getFilePath())) {
            return this.returnResult("文件不存在!", importFileMetaDTO);
        }
        //# 获取文件流，并转换为字节数组，用于反复读取
        log.info("开始从本地获取导入Excel文件流！文件路径：{}", importFileMetaDTO.getFilePath());
        try (
            InputStream inputStream = new BufferedInputStream((new FileInputStream(importFileMetaDTO.getFilePath())))) {
            byteArray = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            log.error("从本地获取导入Excel文件流失败！入参：{}", importFileMetaDTO, e);
            return this.returnResult(ImportStatus.FAIL, e.getMessage());
        }
        return successGetImportFileByteArray(byteArray);
    }

    /**
     * 数据量校验
     *
     * @param statisticsResultDTO
     * @param importFileMetaDTO
     * @return
     */
    private Optional<ImportFileResultDTO> checkDataCount(ExcelReadStatisticsResultDTO statisticsResultDTO,
                                                         ImportFileMetaDTO importFileMetaDTO) {
        // 校验无数据
        if (statisticsResultDTO.getTotalRows() == 0) {
            log.warn("导入的文件中未包含任何数据！fileKey={}", importFileMetaDTO.getFilePath());
            return Optional.of(this.returnResult("导入的文件中未包含任何数据！", importFileMetaDTO));
        }
        // 校验数据超过1万
        if (statisticsResultDTO.getTotalRows() > MAX_DATA_COUNT) {
            String msg = StrUtil.format(IMPORT_MAX_DATA_ROWS_FORMAT, MAX_DATA_COUNT);
            log.warn(msg + "fileKey={}", importFileMetaDTO.getFilePath());
            return Optional.of(this.returnResult(msg, importFileMetaDTO));
        }
        return Optional.empty();
    }

    private void handleDataList(List<ExcelReadTableVO> allReadTableVOS, ExcelReadStatisticsResultDTO statisticsResult,
                                ImportFileMetaDTO importFileMetaDTO) {
        log.info("解析成功，开始对导入数据进行处理！");
        // 循环处理数据
        log.info("开始循环处理数据，总行数：{}，总子表数：{}，批次处理子表数：{}",
                 statisticsResult.getTotalRows(),
                 allReadTableVOS.size(),
                 DEFAULT_BATCH_COUNT);
        batchHandle(allReadTableVOS, importFileMetaDTO);

    }

    private static ExcelReadStatisticsResultDTO getStatisticsResult(List<ExcelReadTableVO> allReadTableVOS) {
        ExcelReadStatisticsResultDTO excelReadStatisticsResultDTO = new ExcelReadStatisticsResultDTO();

        Integer totalRows = allReadTableVOS.stream()
                                           .map(theReadTableVO -> theReadTableVO.getData())
                                           .filter(CollUtil::isNotEmpty)
                                           .map(List::size)
                                           .reduce(Integer::sum)
                                           .orElse(0);
        Integer validateFailRowsRows = allReadTableVOS.stream()
                                                      .map(theReadTableVO -> theReadTableVO.getValidateFailRows())
                                                      .filter(CollUtil::isNotEmpty)
                                                      .map(List::size)
                                                      .reduce(Integer::sum)
                                                      .orElse(0);
        excelReadStatisticsResultDTO.setTotalRows(totalRows);
        excelReadStatisticsResultDTO.setTotalValidateFailRows(validateFailRowsRows);
        if (validateFailRowsRows > 0) {
            excelReadStatisticsResultDTO.setBHasValidateError(true);
        }
        return excelReadStatisticsResultDTO;

    }

    protected abstract List<ExcelSheetReadContext> configExcelSheetReadContext(byte[] buffer);

    public void parsingFile(List<ExcelSheetReadContext> sheetReadContexts) {
        if (CollectionUtils.isEmpty(sheetReadContexts)) {
            log.warn("sheetReadContexts is empty");
            return;
        }
        sheetReadContexts.forEach(ExcelSheetReadContext::read);
    }

    /**
     * 批量处理（减轻数据库压力），
     * 子类实现具体的批量处理逻辑，
     * 例如：保存到数据库，注意事务
     *
     * @param dataList 数据列表
     **/
    public abstract void batchHandle(List<ExcelReadTableVO> dataList, ImportFileMetaDTO importFileMetaDTO);

    /**
     * 导出错误文件
     **/
    public abstract ExportErrorFileResultDTO exportErrorFile(List<ExcelSheetReadContext> sheetReadContexts,
                                                             String sourceFileName);

    /**
     * 默认导出错误文件
     *
     * @param sheetReadContexts
     * @param errorFileTemplatePath
     * @return
     */
    public ExportErrorFileResultDTO defaultExportErrorFile(List<ExcelSheetReadContext> sheetReadContexts,
                                                           String errorFileTemplatePath, String errorFilePath,
                                                           String errorFileExportMode) {
        if (ImportFileSourceEnum.LOCAL.getCode().equals(errorFileExportMode)) {
            return defaultExportErrorFileToLocal(sheetReadContexts, errorFilePath);
        } else {
            return defaultExportErrorFileToOBS(sheetReadContexts, errorFileTemplatePath, errorFilePath);
        }
    }

    /**
     * 默认导出到本地文件
     *
     * @param sheetReadContexts
     * @param errorFilePath
     * @return
     */
    protected ExportErrorFileResultDTO defaultExportErrorFileToLocal(List<ExcelSheetReadContext> sheetReadContexts,
                                                                     String errorFilePath) {
        List<ExcelSheetWriteContextVO> excelSheetWriteContextVOS = convert2ExcelSheetWriteContextVO(sheetReadContexts);
        StringJoiner filePathJoiner = new StringJoiner("/").add("/easyExcel")
                                                           .add(DateUtils.toStr("yyyyMMddHHmmss", new Date()));
        return EasyExcelWriteUtils.multiSheetMultiTableExportToLocal(excelSheetWriteContextVOS,
                                                                     filePathJoiner.toString(),
                                                                     FileUtil.getName(errorFilePath));
    }

    /**
     * 默认导出到OBS
     *
     * @param sheetReadContexts
     * @param errorFileTemplateKey
     * @return
     */
    protected ExportErrorFileResultDTO defaultExportErrorFileToOBS(List<ExcelSheetReadContext> sheetReadContexts,
                                                                   String errorFileTemplateKey, String errorFileKey) {
        List<ExcelSheetWriteContextVO> excelSheetWriteContextVOS = convert2ExcelSheetWriteContextVO(sheetReadContexts);
        return EasyExcelWriteUtils.multiSheetMultiTableExportToObs(excelSheetWriteContextVOS,
                                                                   errorFileTemplateKey,
                                                                   errorFileKey);
    }

    private List<ExcelSheetWriteContextVO> convert2ExcelSheetWriteContextVO(
        List<ExcelSheetReadContext> sheetReadContexts) {
        List<ExcelSheetWriteContextVO> writeContextVOS = sheetReadContexts.stream().map(sheetReadContext -> {
            ExcelSheetWriteContextVO excelSheetWriteContextVO = new ExcelSheetWriteContextVO();
            excelSheetWriteContextVO.setSheetIndex(sheetReadContext.getSheetIndex());
            excelSheetWriteContextVO.setSheetName(sheetReadContext.getSheetName());
            excelSheetWriteContextVO.setHandlers(listCustomCellWriteHandlers());
            List<ExcelWriteTableVO> writeTableVOS = sheetReadContext.getExcelReadTables().stream().map(theReadTable -> {
                List theReadTableData = theReadTable.getData();
                ExcelWriteTableVO writeTableVO = new ExcelWriteTableVO().setTableIndex(theReadTable.getTableIndex())
                                                                        .setClazz(theReadTable.getClazz())
                                                                        .setData(theReadTableData)
                                                                        .setRelativeHeadRowIndex(theReadTable.getRelativeHeadRowIndex());
                //子表没数据，直接返回
                if (CollUtil.isEmpty(theReadTableData)) {
                    return writeTableVO;
                }
                //设置每行的校验错误
                List<ValidateFailRow> validateFailRows = theReadTable.getValidateFailRows();
                Map<Integer, String> theRowIndex2ErrorMsgMap = validateFailRows.stream()
                                                                               .collect(Collectors.toMap(ValidateFailRow::getTableRowIndex,
                                                                                                         ValidateFailRow::getErrorMsg));
                for (Map.Entry<Integer, String> theRowIndex2ErrorMsgEntry : theRowIndex2ErrorMsgMap.entrySet()) {
                    Integer tableRowIndex = theRowIndex2ErrorMsgEntry.getKey();
                    String errorMsg = theRowIndex2ErrorMsgEntry.getValue();
                    try {
                        ValidateErrorHead validateErrorHead = (ValidateErrorHead)theReadTableData.get(tableRowIndex);
                        validateErrorHead.setValidateResult(errorMsg);
                    } catch (Exception e) {
                        log.error("设置校验结果列的校验错误时，失败", e);
                    }
                }
                return writeTableVO;
            }).collect(Collectors.toList());
            excelSheetWriteContextVO.setTables(writeTableVOS);
            return excelSheetWriteContextVO;
        }).collect(Collectors.toList());
        return writeContextVOS;
    }

    /**
     * 写样式
     *
     * @return
     */
    public List<WriteHandler> listCustomCellWriteHandlers() {
        return new ArrayList<>();
    }

    /**
     * 成功解析导入文件
     *
     * @param byteArray
     * @return
     */
    private ImportFileResultDTO successGetImportFileByteArray(byte[] byteArray) {
        return ImportFileResultDTO.builder().importStatus(ImportStatus.ING).byteArray(byteArray).build();
    }

    private ImportFileResultDTO returnResult(String detail, ImportFileMetaDTO importFileMetaDTO) {
        log.error("{}，入参：【{}】", detail, importFileMetaDTO);
        return this.returnResult(ImportStatus.FAIL, detail);
    }

    private ImportFileResultDTO returnResult(ImportStatus importStatus, String detail) {
        return returnResult(importStatus, detail, null, null);
    }

    private ImportFileResultDTO returnResult(ImportStatus importStatus, String detail, String feedbackFileName,
                                             String feedbackFileKey) {
        return ImportFileResultDTO.builder()
                                  .importStatus(importStatus)
                                  .detail(detail)
                                  .feedbackFileName(feedbackFileName)
                                  .feedbackFileKey(feedbackFileKey)
                                  .build();
    }
}
