package com.xkcoding.excel.multiwrite;

import com.xkcoding.common.utils.DateUtils;
import com.xkcoding.excel.multiwrite.constants.ExcelPathConstants;
import com.xkcoding.excel.multiwrite.dto.ExcelReadTableVO;
import com.xkcoding.excel.multiwrite.dto.ExcelSheetReadContext;
import com.xkcoding.excel.multiwrite.dto.ExportErrorFileResultDTO;
import com.xkcoding.excel.multiwrite.dto.ImportFileMetaDTO;
import com.xkcoding.excel.multiwrite.enums.ImportFileTemplateEnum;
import com.xkcoding.excel.multiwrite.sheetconfig.read.ExcelSheetReadContextConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * @Author XuGang
 * @Date 2024/3/8 18:13
 */
@Slf4j
@Service
public class TmpAbstractImportFileTemplateImpl extends AbstractImportFileTemplate {
    @Value(value = "${excel.export.error.mode:OBS}")
    private String errorFileExportMode;

    @Autowired
    private List<ExcelSheetReadContextConfig> excelSheetReadContextConfigs;

    @Override
    public ImportFileTemplateEnum getTemplateEnum() {
        return ImportFileTemplateEnum.TEST;
    }

    @Override
    protected List<ExcelSheetReadContext> configExcelSheetReadContext(byte[] buffer) {
        return excelSheetReadContextConfigs.stream()
                                           .filter(theSheetConfig -> getTemplateEnum().equals(theSheetConfig.getTemplateEnum()))
                                           .map(theSheetConfig -> theSheetConfig.configExcelSheetReadContext(buffer))
                                           .collect(Collectors.toList());
    }

    @Override
    public void batchHandle(List<ExcelReadTableVO> dataList, ImportFileMetaDTO importFileMetaDTO) {
    }

    /**
     * 批量保存（减轻数据库压力），
     * 子类实现具体的批量处理逻辑，
     * 例如：保存到数据库，注意事务
     *
     * @param excelReadTableVOList
     * @param importFileMetaDTO
     */

    @Transactional(timeout = 10, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void batchSave(List<ExcelReadTableVO> excelReadTableVOList, ImportFileMetaDTO importFileMetaDTO) {

    }

    @Override
    public ExportErrorFileResultDTO exportErrorFile(List<ExcelSheetReadContext> sheetReadContexts,
                                                    String sourceFileName) {
        Date currentDate = new Date();
        String currentDateStr = "";
        StringJoiner errorFileNameJoiner = new StringJoiner("+");
        StringJoiner errorFilePathJoiner = new StringJoiner("/");
        errorFileNameJoiner.add(sourceFileName).add("错误文件").add(currentDateStr + ".xlsx");
        return defaultExportErrorFile(sheetReadContexts,
                                      ExcelPathConstants.DATA_IMPORT_MONTH_REPORT_TEMPLATE,
                                      errorFilePathJoiner.add(ExcelPathConstants.DATA_IMPORT_ERROR_PREFIX_PATH)
                                                         .add(DateUtils.toStr("yyyy-MM-dd", currentDate))
                                                         .add(errorFileNameJoiner.toString())
                                                         .toString(),
                                      errorFileExportMode);
    }
}