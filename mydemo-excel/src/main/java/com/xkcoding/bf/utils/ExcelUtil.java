package com.xkcoding.bf.utils;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.xkcoding.bf.dto.WaterMarkDTO;
import com.xkcoding.bf.handler.CustomCellWriteHeightHandler;
import com.xkcoding.bf.handler.CustomCellWriteWeightHandler;
import com.xkcoding.bf.handler.CustomWaterMarkHandler;
import com.xkcoding.bf.handler.ExcelMergeStrategy;
import io.swagger.annotations.ApiModel;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

/**
 * excel导入导出工具
 *
 * @author hangaoping
 * @date 2022/11/29
 */
public class ExcelUtil {

    /**
     * 带水印、带合并头部的excel导出
     *
     * @param data         数据
     * @param clazz        提供列字段
     * @param watermark    水印
     * @param fileName     文件名
     * @param bMergeHeader 是否合并头部
     * @throws Exception
     */
    public static void exportExcel(Collection<?> data, Class clazz, WaterMarkDTO watermark, OutputStream out,
                                   String fileName, Boolean bMergeHeader) throws Exception {
        ApiModel apiModel = (ApiModel)clazz.getAnnotation(ApiModel.class);
        Conditions.assertTrue(Objects.nonNull(apiModel), "请提供ApiModel注解");
        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(out);
        excelWriterBuilder.head(clazz).inMemory(true);
        if (bMergeHeader) {
            excelWriterBuilder.registerWriteHandler(new ExcelMergeStrategy(clazz));
        }
        excelWriterBuilder.sheet(apiModel.value())
                          .registerWriteHandler(new CustomWaterMarkHandler(watermark))
                          .registerWriteHandler(new CustomCellWriteWeightHandler())
                          .registerWriteHandler(new CustomCellWriteHeightHandler())
                          .doWrite(data);
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
        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(response.getOutputStream());
        excelWriterBuilder.head(clazz).inMemory(true);
        if (bMergeHeader) {
            excelWriterBuilder.registerWriteHandler(new ExcelMergeStrategy(clazz));
        }
        excelWriterBuilder.sheet(apiModel.value())
                          .registerWriteHandler(new CustomWaterMarkHandler(watermark))
                          .registerWriteHandler(new CustomCellWriteWeightHandler())
                          .registerWriteHandler(new CustomCellWriteHeightHandler())
                          .doWrite(data);
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
}
