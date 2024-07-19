package com.xkcoding.excel.exceltool.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.google.common.collect.Lists;
import com.xkcoding.excel.exceltool.dropdown.annotation.ChainDropDownFields;
import com.xkcoding.excel.exceltool.dropdown.annotation.DropDownFields;
import com.xkcoding.excel.exceltool.dropdown.handler.ChainDropDownWriteHandler;
import com.xkcoding.excel.exceltool.dropdown.handler.DropDownAdvicedWriteHandler;
import com.xkcoding.excel.exceltool.dropdown.handler.ResolveAnnotation;
import com.xkcoding.excel.exceltool.entity.ChainDropDownResolved;
import com.xkcoding.excel.exceltool.entity.DropDownResolved;
import com.xkcoding.excel.exceltool.listener.BaseExcelDataListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * easyExcel 工具类
 *
 * @author rstyro
 */
@Slf4j
public class EasyExcelUtils {

    /**
     * 浏览器导出excel文件
     * 官网文档地址：https://www.yuque.com/easyexcel/doc/easyexcel
     *
     * @param data          数据
     * @param templateClass 模板对象class
     * @param pageSize      每页多少条
     * @param fileName      文件名称
     * @param response      输出流
     * @throws Exception err
     */
    public static void exportBrowser(List data, Class templateClass, Integer pageSize, String fileName,
                                     HttpServletResponse response) throws Exception {
        pageSize = Optional.ofNullable(pageSize).orElse(50000);
        fileName = fileName + System.currentTimeMillis();
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        ExcelWriter excelWriter = null;
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            //// 获取改类声明的所有字段
            //Field[] fields = templateClass.getDeclaredFields();
            //// 响应字段对应的下拉集合
            //Map<Integer, String[]> map = processDropDown(fields);
            //Map<Integer, ChainDropDown> integerChainDropDownMap = processChainDropDown(fields);

            //excelWriter = EasyExcel.write(out, templateClass)
            //                       .registerWriteHandler(new DropDownWriteHandler(map))
            //                       .registerWriteHandler(new ChainDropDownWriteHandler(integerChainDropDownMap))
            //                       .build();

            // 响应字段对应的下拉集合
            Map<Integer, DropDownResolved> dropDownFieldsResolvedMap = processDropDown(templateClass);
            Map<Integer, ChainDropDownResolved> integerChainDropDownMap = processChainDropDown(templateClass);
            excelWriter = EasyExcel.write(out, templateClass)
                                   .registerWriteHandler(new DropDownAdvicedWriteHandler(dropDownFieldsResolvedMap))
                                   .registerWriteHandler(new ChainDropDownWriteHandler(integerChainDropDownMap))
                                   .build();
            // 分页写入
            pageWrite(excelWriter, data, pageSize);
        } catch (Throwable e) {
            response.setHeader("Content-Disposition", "attachment;filename=下载失败");
            e.printStackTrace();
            log.error("文档下载失败:" + e.getMessage());
        } finally {
            data.clear();
            if (excelWriter != null) {
                excelWriter.finish();
            }
            assert out != null;
            out.flush();
            out.close();
        }
    }

    /**
     * 分页写入
     *
     * @param writer   ExcelWriter
     * @param data     数据
     * @param pageSize 分页大小
     */
    public static void pageWrite(ExcelWriter writer, List<Object> data, Integer pageSize) {
        List<List<Object>> lt = Lists.partition(data, pageSize);
        for (int i = 0; i < lt.size(); i++) {
            int j = i + 1;
            WriteSheet writeSheet = EasyExcel.writerSheet(i, "第" + j + "页").build();
            writer.write(lt.get(i), writeSheet);
        }
    }

    /**
     * 读取excel 并解析
     *
     * @param file  文件
     * @param clazz 解析成哪个dto
     * @param <T>   t
     * @return list
     * @throws IOException error
     */
    public static <T> List<T> read(MultipartFile file, Class<T> clazz) throws IOException {
        BaseExcelDataListener<T> baseExcelDataListener = new BaseExcelDataListener();
        EasyExcel.read(file.getInputStream(), clazz, baseExcelDataListener).sheet().doRead();
        return baseExcelDataListener.getResult();
    }

    /**
     * 处理有下拉框注解的属性
     * <p>
     * 解析表头类中的下拉注解
     *
     * @param templateClass 表头类
     * @param <T>           泛型
     * @return Map<下拉框列索引, 下拉框内容> map
     */
    public static <T> Map<Integer, DropDownResolved> processDropDown(Class<T> templateClass) {
        Map<Integer, DropDownResolved> selectedMap = new HashMap<>();
        Field[] fields = templateClass.getDeclaredFields();
        // 响应字段对应的下拉集合
        Map<Integer, String[]> map = new HashMap<>();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            // 解析注解信息
            DropDownFields selected = field.getAnnotation(DropDownFields.class);
            ExcelProperty property = field.getAnnotation(ExcelProperty.class);
            if (selected != null) {
                DropDownResolved dropDownResolved = new DropDownResolved();
                // 处理下拉框内容
                String[] source = dropDownResolved.resolveDropDownFields(selected);
                if (source != null && source.length > 0) {
                    dropDownResolved.setSource(source);
                    dropDownResolved.setFirstRow(selected.firstRow());
                    dropDownResolved.setLastRow(selected.lastRow());
                    if (property != null && property.index() >= 0) {
                        selectedMap.put(property.index(), dropDownResolved);
                    } else {
                        selectedMap.put(i, dropDownResolved);
                    }
                }
            }
        }

        return selectedMap;
    }

    /**
     * 处理有下拉框注解的属性
     *
     * @param fields 字段属性集合
     * @return map
     */
    public static Map<Integer, String[]> processDropDown(Field[] fields) {
        // 响应字段对应的下拉集合
        Map<Integer, String[]> map = new HashMap<>();
        Field field = null;
        // 循环判断哪些字段有下拉数据集，并获取
        for (int i = 0; i < fields.length; i++) {
            field = fields[i];
            // 解析注解信息
            DropDownFields dropDownField = field.getAnnotation(DropDownFields.class);
            if (null != dropDownField) {
                String[] sources = ResolveAnnotation.resolve(dropDownField);
                if (null != sources && sources.length > 0) {
                    map.put(i, sources);
                }
            }
        }
        return map;
    }

    public static <T> Map<Integer, ChainDropDownResolved> processChainDropDown(Class<T> templateClass) {
        Field[] fields = templateClass.getDeclaredFields();
        // 响应字段对应的下拉集合
        Map<Integer, ChainDropDownResolved> map = new HashMap<>();
        Field field = null;
        int rowIndex = 0;
        // 循环判断哪些字段有下拉数据集，并获取
        for (int i = 0; i < fields.length; i++) {
            field = fields[i];
            // 解析注解信息
            ChainDropDownFields chainDropDownFields = field.getAnnotation(ChainDropDownFields.class);
            if (null != chainDropDownFields) {
                //                List<ChainDropDown> sources = ResolveAnnotation.resolve(chainDropDownFields);
                ChainDropDownResolved resolve = ResolveAnnotation.resolve(chainDropDownFields);
                resolve.setFirstRow(chainDropDownFields.firstRow());
                resolve.setLastRow(chainDropDownFields.lastRow());
                long collect = resolve.getDataMap().keySet().size();
                System.out.println("collect=" + collect);
                if (resolve.isRootFlag()) {
                    resolve.setRowIndex(rowIndex);
                    rowIndex += 1;
                } else {
                    resolve.setRowIndex(rowIndex);
                    rowIndex += collect;
                }
                if (!ObjectUtils.isEmpty(resolve)) {
                    map.put(i, resolve);
                }
            }
        }
        return map;
    }

    public static Map<Integer, ChainDropDownResolved> processChainDropDown(Field[] fields) {
        // 响应字段对应的下拉集合
        Map<Integer, ChainDropDownResolved> map = new HashMap<>();
        Field field = null;
        int rowIndex = 0;
        // 循环判断哪些字段有下拉数据集，并获取
        for (int i = 0; i < fields.length; i++) {
            field = fields[i];
            // 解析注解信息
            ChainDropDownFields chainDropDownFields = field.getAnnotation(ChainDropDownFields.class);
            if (null != chainDropDownFields) {
                //                List<ChainDropDown> sources = ResolveAnnotation.resolve(chainDropDownFields);
                ChainDropDownResolved resolve = ResolveAnnotation.resolve(chainDropDownFields);
                long collect = resolve.getDataMap().keySet().size();
                System.out.println("collect=" + collect);
                if (resolve.isRootFlag()) {
                    resolve.setRowIndex(rowIndex);
                    rowIndex += 1;
                } else {
                    resolve.setRowIndex(rowIndex);
                    rowIndex += collect;
                }
                if (!ObjectUtils.isEmpty(resolve)) {
                    map.put(i, resolve);
                }
            }
        }
        return map;
    }

    /**
     * 获取Excel列的号码A-Z - AA-ZZ - AAA-ZZZ 。。。。
     *
     * @param num
     * @return
     */
    public static String getColNum(int num) {
        int MAX_NUM = 26;
        char initChar = 'A';
        if (num == 0) {
            return initChar + "";
        } else if (num > 0 && num < MAX_NUM) {
            int result = num % MAX_NUM;
            return (char)(initChar + result) + "";
        } else if (num >= MAX_NUM) {
            int result = num / MAX_NUM;
            int mod = num % MAX_NUM;
            String starNum = getColNum(result - 1);
            String endNum = getColNum(mod);
            return starNum + endNum;
        }
        return "";
    }
}
