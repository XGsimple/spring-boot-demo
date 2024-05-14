package com.xkcoding.bf.utils;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xkcoding.bf.dto.WaterMarkDTO;
import com.xkcoding.bf.handler.CustomCellWriteHeightHandler;
import com.xkcoding.bf.handler.CustomCellWriteWeightHandler;
import com.xkcoding.bf.handler.CustomWaterMarkHandler;
import com.xkcoding.bf.handler.ExcelMergeStrategy;
import io.swagger.annotations.ApiModel;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.function.Function;

/**
 * excel导入导出工具
 *
 * @author hangaoping
 * @date 2022/11/29
 */
public class ExcelUtil {

    /**
     * 带水印、带合并头部的excel导出
     * <p>
     * 导出水印，只能设置inMemory(true)，但这样会导致导出时速度，很慢。
     * 因此使用空白Excel写入水印，作为模板。真正导出大量数据时，去除inMemory(true)，加快导出数据
     *
     * @param data         数据
     * @param clazz        提供列字段
     * @param watermark    水印
     * @param bMergeHeader 是否合并头部
     * @throws Exception
     */
    public static void exportExcel(Collection<?> data, Class clazz, WaterMarkDTO watermark, OutputStream out,
                                   Boolean bMergeHeader) throws Exception {
        ApiModel apiModel = (ApiModel)clazz.getAnnotation(ApiModel.class);
        Conditions.assertTrue(Objects.nonNull(apiModel), "请提供ApiModel注解");
        ExcelWriterSheetBuilder writerSheetBuilder = EasyExcel.writerSheet(apiModel.value())
                                                              .registerWriteHandler(new CustomCellWriteWeightHandler())
                                                              .registerWriteHandler(new CustomCellWriteHeightHandler());
        if (bMergeHeader) {
            writerSheetBuilder.registerWriteHandler(new ExcelMergeStrategy(clazz));
        }
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            //使用空白Excel写入水印，作为模板，加快导出数据
            try (ExcelWriter tmpWaterMarkExcelWriter = EasyExcel.write(baos)
                                                                .needHead(false)
                                                                .inMemory(true)
                                                                .registerWriteHandler(new CustomWaterMarkHandler(
                                                                    watermark))
                                                                .build()) {
                tmpWaterMarkExcelWriter.write(new ArrayList<>(), writerSheetBuilder.build());
            }
            //去除inMemory(true)，加快导出数据
            try (ExcelWriter excelWriter = EasyExcel.write(out)
                                                    .withTemplate(new ByteArrayInputStream(baos.toByteArray()))
                                                    .head(clazz)
                                                    .build()) {
                excelWriter.write(data, writerSheetBuilder.build());
            }
        }
    }

    /**
     * 带水印、带合并头部的excel导出
     *
     * @param data         数据
     * @param clazz        提供列字段
     * @param watermark    水印
     * @param response     响应
     * @param fileName     文件名
     * @param bMergeHeader 是否合并头部
     * @throws Exception
     */
    public static void exportExcel(Collection<?> data, Class clazz, WaterMarkDTO watermark,
                                   HttpServletResponse response, String fileName, Boolean bMergeHeader)
        throws Exception {
        ApiModel apiModel = (ApiModel)clazz.getAnnotation(ApiModel.class);
        Conditions.assertTrue(Objects.nonNull(apiModel), "请提供ApiModel注解");
        setExcelResponseProp(response, fileName);
        ExcelWriterSheetBuilder writerSheetBuilder = EasyExcel.writerSheet(apiModel.value())
                                                              .registerWriteHandler(new CustomCellWriteWeightHandler())
                                                              .registerWriteHandler(new CustomCellWriteHeightHandler());
        if (bMergeHeader) {
            writerSheetBuilder.registerWriteHandler(new ExcelMergeStrategy(clazz));
        }
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ExcelWriter tmpWaterMarkExcelWriter = EasyExcel.write(baos)
                                                                .needHead(false)
                                                                .inMemory(true)
                                                                .registerWriteHandler(new CustomWaterMarkHandler(
                                                                    watermark))
                                                                .build()) {
                tmpWaterMarkExcelWriter.write(new ArrayList<>(), writerSheetBuilder.build());
            }
            try (OutputStream outputStream = response.getOutputStream()) {
                try (ExcelWriter excelWriter = EasyExcel.write(outputStream)
                                                        .withTemplate(new ByteArrayInputStream(baos.toByteArray()))
                                                        .head(clazz)
                                                        .build()) {
                    excelWriter.write(data, writerSheetBuilder.build());
                }
            }
        }
    }

    /**
     * 带默认水印、带合并头部的excel导出
     *
     * @param data
     * @param clazz
     * @param response
     * @throws Exception
     */
    public static void exportExcel(Collection<?> data, Class clazz, HttpServletResponse response, String fileName)
        throws Exception {
        exportExcel(data, clazz, getDefaultWaterMark(), response, fileName, true);
    }

    /**
     * 带水印、带合并头部的excel导出
     *
     * @param data
     * @param clazz
     * @param watermark
     * @param response
     * @throws Exception
     */
    public static void exportExcel(Collection<?> data, Class clazz, WaterMarkDTO watermark,
                                   HttpServletResponse response, String fileName) throws Exception {
        exportExcel(data, clazz, watermark, response, fileName, true);
    }

    /**
     * 带水印、不合并头部的excel导出
     *
     * @param data
     * @param clazz
     * @param watermark
     * @param response
     * @throws Exception
     */
    public static void exportExcelNotMerge(Collection<?> data, Class clazz, WaterMarkDTO watermark,
                                           HttpServletResponse response, String fileName) throws Exception {
        exportExcel(data, clazz, watermark, response, fileName, false);
    }

    /**
     * 自定义头部excel导出
     *
     * @param heads     自定义的头
     * @param allList   数据（二维list）一维是行二维是列
     * @param response
     * @param fileName  excel文件名称
     * @param watermark 水印
     * @param sheetName sheet页名称
     * @throws Exception
     */
    public static void dynamicExportExcel(List<String> heads, List<List<String>> allList, HttpServletResponse response,
                                          String fileName, String sheetName, WaterMarkDTO watermark) throws Exception {
        setExcelResponseProp(response, fileName);
        ExcelWriterSheetBuilder writerSheetBuilder = EasyExcel.writerSheet(sheetName)
                                                              .registerWriteHandler(new CustomCellWriteWeightHandler())
                                                              .registerWriteHandler(new CustomCellWriteHeightHandler());
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            //
            try (ExcelWriter tmpWaterMarkExcelWriter = EasyExcel.write(baos)
                                                                .needHead(false)
                                                                .inMemory(true)
                                                                .registerWriteHandler(new CustomWaterMarkHandler(
                                                                    watermark))
                                                                .build()) {
                tmpWaterMarkExcelWriter.write(new ArrayList<>(), writerSheetBuilder.build());
            }
            try (OutputStream outputStream = response.getOutputStream()) {
                try (ExcelWriter excelWriter = EasyExcel.write(outputStream)
                                                        .withTemplate(new ByteArrayInputStream(baos.toByteArray()))
                                                        .head(headers(heads))
                                                        .build()) {
                    excelWriter.write(allList, writerSheetBuilder.build());
                }
            }
        }
    }

    /**
     * 带默认水印、不合并头部的excel导出
     *
     * @param data
     * @param clazz
     * @param response
     * @throws Exception
     */
    public static void exportExcelNotMerge(Collection<?> data, Class clazz, HttpServletResponse response,
                                           String fileName) throws Exception {
        exportExcelNotMerge(data, clazz, getDefaultWaterMark(), response, fileName);
    }

    public static <T> void batchExportExcel(Collection<T> data, Class<T> clazz, WaterMarkDTO watermark,
                                            Function<Page, List<T>> func, HttpServletResponse response) {
        //文件名
        String fileName = String.valueOf(System.currentTimeMillis());
        //记录总数:实际中需要根据查询条件进行统计即可:一共多少条
        int totalCount = 10_000;
        //每一个Sheet存放100w条数据
        Integer sheetDataRows = 5_000;
        //每次写入的数据量20w,每页查询20W
        Integer writeDataRows = 5_000;
        //计算需要的Sheet数量
        Integer sheetNum = (totalCount - 1) / sheetDataRows + 1;
        Integer totalPage = (totalCount - 1) / writeDataRows + 1;
        //必须放到循环外，否则会刷新流
        Page page = new Page(1, writeDataRows);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ExcelWriter excelWriter = EasyExcel.write(baos)
                                               .excelType(ExcelTypeEnum.XLSX)
                                               .registerWriteHandler(new CustomWaterMarkHandler(watermark))
                                               .inMemory(true)
                                               .build();
            //开始分批查询分次写入
            for (int i = 0; i < sheetNum; i++) {
                //创建Sheet
                WriteSheet sheet = new WriteSheet();
                sheet.setSheetName("Sheet" + i);
                sheet.setSheetNo(i);
                //循环写入次数: j的自增条件是当不是最后一个Sheet的时候写入次数为正常的每个Sheet写入的次数,如果是最后一个就需要使用计算的次数lastSheetWriteCount
                for (int j = ((int)(page.getCurrent())); j <= totalPage; j++) {
                    WriteSheet writeSheet = EasyExcel.writerSheet(i, "Sheet" + (i + 1))
                                                     .needHead(false)
                                                     .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                                                     .build();
                    excelWriter.write(new ArrayList<>(), writeSheet);
                    if (j * writeDataRows % sheetDataRows == 0) {
                        //记录当前页数j并加1,并跳出这个for循环,往下一个sheet页写入数据
                        page.setCurrent(j + 1);
                        break;
                    }
                }
            }
            excelWriter.finish();

            //开始分批查询分次写入
            page.setCurrent(1);
            try (OutputStream rspOutputStream = response.getOutputStream()) {
                ExcelWriter excelWriter1 = EasyExcel.write(rspOutputStream)
                                                    .withTemplate(new ByteArrayInputStream(baos.toByteArray()))
                                                    .excelType(ExcelTypeEnum.XLSX)
                                                    .build();
                for (int i = 0; i < sheetNum; i++) {
                    //创建Sheet
                    WriteSheet sheet = new WriteSheet();
                    sheet.setSheetName("Sheet" + i);
                    sheet.setSheetNo(i);
                    //循环写入次数: j的自增条件是当不是最后一个Sheet的时候写入次数为正常的每个Sheet写入的次数,如果是最后一个就需要使用计算的次数lastSheetWriteCount
                    for (int j = ((int)(page.getCurrent())); j <= totalPage; j++) {
                        List<T> list = func.apply(page);
                        WriteSheet writeSheet = EasyExcel.writerSheet(i, "Sheet" + (i + 1))
                                                         .head(clazz)
                                                         .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                                                         .build();
                        excelWriter1.write(list, writeSheet);
                        if (j * writeDataRows % sheetDataRows == 0) {
                            //记录当前页数j并加1,并跳出这个for循环,往下一个sheet页写入数据
                            page.setCurrent(j + 1);
                            break;
                        }
                    }
                }
                // 下载EXCEL，返回给前段stream流
                response.setContentType("application/octet-stream");
                response.setCharacterEncoding("utf-8");
                response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
                excelWriter1.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setExcelResponseProp(HttpServletResponse response, String rawFileName)
        throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(rawFileName + DateUtil.format(new Date(), "yyyyMMddHHmmss"));
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
    }

    /**
     * 默认水印
     *
     * @return
     */
    private static WaterMarkDTO getDefaultWaterMark() {
        WaterMarkDTO watermark = new WaterMarkDTO();
        watermark.setContent("测试水印");
        return watermark;
    }

    private static List<List<String>> headers(List<String> heads) {
        List<List<String>> headers = new ArrayList<>();
        for (String header : heads) {
            List<String> head = new ArrayList<>();
            head.add(header);
            headers.add(head);
        }
        return headers;
    }
}
