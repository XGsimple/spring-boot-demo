package com.xkcoding.bf.write;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.builder.ExcelWriterTableBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.xkcoding.bf.ObsFileService;
import com.xkcoding.bf.constants.CommonConstants;
import com.xkcoding.bf.dto.ExportErrorFileResultDTO;
import com.xkcoding.bf.dto.WaterMarkDTO;
import com.xkcoding.bf.handler.CustomCellWriteHeightHandler;
import com.xkcoding.bf.handler.CustomCellWriteWeightHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.*;

/**
 * easyExcel工具
 */
@Slf4j
public class EasyExcelWriteUtils {
    public static final Map<String, List<ExcelAnnotationValue>> annotationValues = new HashMap<>();

    private static File getExcelFile(String filePath, String fileName) {
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (StrUtil.isBlank(FileUtil.getSuffix(fileName))) {
                filePath += ".xlsx";
            }
            File file = new File(filePath + "/" + fileName);
            if (file.exists()) {
                file.deleteOnExit();
            }
            file.createNewFile();
            log.info("filePath={}", filePath);
            return file;
        } catch (Exception e) {
            log.error("创建本地Excel错误文件失败！", e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static ExportErrorFileResultDTO multiSheetMultiTableExportToLocal(List<ExcelSheetWriteContextVO> entities,
                                                                             String errorFilePath, String errFileName) {
        File errorExcelFile = getExcelFile(errorFilePath, errFileName);
        try (BufferedOutputStream outputStream = FileUtil.getOutputStream(errorExcelFile)) {
            multiSheetMultiTableExport(entities, outputStream);
        } catch (Exception e) {
            log.error("导出文件失败", e);
        }
        return ExportErrorFileResultDTO.builder().name(errorFilePath).build();
    }

    public static ExportErrorFileResultDTO multiSheetMultiTableExportToObs(List<ExcelSheetWriteContextVO> entities,
                                                                           String errorFileTemplateKey,
                                                                           String errorFileKey) {
        ExportErrorFileResultDTO.ExportErrorFileResultDTOBuilder builder = ExportErrorFileResultDTO.builder();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            //生成导出Excel输出流
            multiSheetMultiTableExport(entities, outputStream);
            //上传错误文件到OBS
            try (InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray())) {
                PutObjectResult result = ObsFileService.putObjectByInput(ObsFileService.getBucketName(),
                                                                         errorFileKey,
                                                                         inputStream);
                if (result.getStatusCode() != 200) {
                    log.error("上传excel文件失败：{}", result.getStatusCode());
                    throw new IllegalStateException("upload excel to obs fail");
                }
                builder.name(errorFileKey).key(result.getObjectKey());
            } catch (Exception e) {
                log.error("上传错误Excel文件到OBS失败，errorFileTemplateKey={}，errorFileKey={}",
                          errorFileTemplateKey,
                          errorFileKey,
                          e);
            }
        } catch (Exception e) {
            log.error("导出错误Excel文件失败，errorFileTemplateKey={}，errorFileKey={}",
                      errorFileTemplateKey,
                      errorFileKey,
                      e);
        }
        return builder.build();
    }

    /**
     * 导出Excel文件
     *
     * @param entities
     * @param outputStream
     */
    private static void multiSheetMultiTableExport(List<ExcelSheetWriteContextVO> entities, OutputStream outputStream) {
        try (ExcelWriter excelWriter = EasyExcel.write(outputStream).build()) {
            //按照sheet顺序排序
            entities.sort(Comparator.comparing(ExcelSheetWriteContextVO::getSheetIndex));
            for (int i = 0; i < entities.size(); i++) {
                ExcelSheetWriteContextVO theSheet = entities.get(i);
                List<ExcelWriteTableVO> tables = theSheet.getTables();
                String sheetName = theSheet.getSheetName();
                List<WriteHandler> handlers = theSheet.getHandlers();
                ExcelWriterSheetBuilder sheetBuilder = EasyExcel.writerSheet(i, sheetName);
                if (!ObjectUtils.isEmpty(handlers)) {
                    for (WriteHandler handler : handlers) {
                        sheetBuilder.registerWriteHandler(handler);
                    }
                }
                //自适应列宽行高
                sheetBuilder.registerWriteHandler(new CustomCellWriteWeightHandler())
                            .registerWriteHandler(new CustomCellWriteHeightHandler());
                //创建sheet
                WriteSheet writeSheet = sheetBuilder.build();
                //创建table
                Assert.isTrue(!ObjectUtils.isEmpty(tables), "缺少table数据");
                //按照子表顺序排序
                tables.sort(Comparator.comparing(ExcelWriteTableVO::getTableIndex));
                //写子表数据
                for (int j = 0; j < tables.size(); j++) {
                    ExcelWriteTableVO tableEntity = tables.get(j);
                    Map<String, String> vars = tableEntity.getVars();
                    List<?> date = tableEntity.getData();
                    Class<?> clazz = tableEntity.getClazz();
                    resetCLassExcelPropertyAnnotationValue(clazz);
                    setFieldExcelPropertyAnnotationValue(clazz, vars);
                    ExcelWriterTableBuilder tableBuilder = EasyExcel.writerTable(j);

                    if (j > 0) {
                        tableBuilder.relativeHeadRowIndex(tableEntity.getRelativeHeadRowIndex());
                    }
                    WriteTable table = tableBuilder.head(clazz).needHead(true).build();
                    excelWriter.write(date, writeSheet, table);
                }
            }
        } catch (Exception e) {
            log.error("导出Excel错误文件失败！", e);
        }
    }

    /**
     * 动态字段上ExcelProperty替换表头
     *
     * @param clazz
     * @param map
     */
    public static void setFieldExcelPropertyAnnotationValue(Class<?> clazz, Map<String, String> map) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ExcelProperty property = field.getAnnotation(ExcelProperty.class);
            if (property != null) {
                List<String> newValues = new ArrayList<>();
                String[] values = property.value();
                for (String value : values) {
                    value = replace(value, map);
                    newValues.add(value);
                }
                InvocationHandler h = Proxy.getInvocationHandler(property);
                try {
                    Field annotationField = h.getClass().getDeclaredField("memberValues");
                    annotationField.setAccessible(true);
                    Map memberValues = (Map)annotationField.get(h);
                    memberValues.put("value", newValues.toArray(new String[] {}));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 重置类上ExcelProperty注解值
     *
     * @param clazz
     */
    private static void resetCLassExcelPropertyAnnotationValue(Class<?> clazz) {
        String className = clazz.getName();
        List<ExcelAnnotationValue> values = annotationValues.get(className);
        if (ObjectUtils.isEmpty(values)) {
            //如果静态资源是空的，保存
            Field[] fields = clazz.getDeclaredFields();
            values = new ArrayList<>();
            for (Field field : fields) {
                ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
                if (!ObjectUtils.isEmpty(excelProperty)) {
                    String[] vs = excelProperty.value();
                    ExcelAnnotationValue value = new ExcelAnnotationValue().setFieldName(field.getName()).setValues(vs);
                    values.add(value);
                }
            }
            annotationValues.put(className, values);
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            if (!ObjectUtils.isEmpty(excelProperty)) {
                ExcelAnnotationValue value = values.stream()
                                                   .filter(v -> v.getFieldName().equals(fieldName))
                                                   .findFirst()
                                                   .orElse(null);
                if (!ObjectUtils.isEmpty(value)) {
                    String[] oldValues = value.getValues();
                    InvocationHandler handler = Proxy.getInvocationHandler(excelProperty);
                    try {
                        Field annotationField = handler.getClass().getDeclaredField("memberValues");
                        annotationField.setAccessible(true);
                        Map memberValues = (Map)annotationField.get(handler);
                        memberValues.put("value", oldValues);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static String replace(String el, Map<String, String> map) {
        if (map == null) {
            return el;
        }
        String evl = el;
        for (Map.Entry<String, String> m : map.entrySet()) {
            String key = m.getKey();
            String value = m.getValue();
            el = el.replaceAll("#\\{" + key + "\\}", value);
            if (!evl.equals(el)) {
                return el;
            }
        }
        return el;
    }

    /**
     * 默认水印
     *
     * @return
     */
    private static WaterMarkDTO getDefaultWaterMark() {
        WaterMarkDTO watermark = new WaterMarkDTO();
        watermark.setContent(CommonConstants.WaterMark.defaultTemple);
        return watermark;
    }
}
