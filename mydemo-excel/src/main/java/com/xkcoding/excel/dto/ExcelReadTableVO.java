package com.xkcoding.bf.dto;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.xkcoding.bf.easylistener.RuleCheckListener;
import com.xkcoding.bf.enums.Module;
import com.xkcoding.bf.enums.SubModule;
import com.xkcoding.bf.handler.TableDataReadHandler;
import com.xkcoding.bf.handler.TableRowDataReadHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * @Author XuGang
 * @Date 2024/3/7 13:53
 */
@Slf4j
@Setter
@Getter
@Accessors(chain = true)
public class ExcelReadTableVO<T> implements ExcelReadTable<T> {

    /**
     * 子表对应模型Class
     */
    private Class<T> clazz;

    /**
     * sheet页号
     */
    private Integer sheetIndex;

    /**
     * sheet名称
     */
    private String sheetCode;

    /**
     * sheet名称
     */
    private String sheetName;

    /**
     * 子表模块名
     */
    private String tableModuleName;

    /**
     * 子表编码
     */
    private String tableModuleCode;

    /**
     * sheet页中子表顺序
     */
    private Integer tableIndex;

    /**
     * 子表名
     */
    private String tableCode;

    /**
     * 子表名
     */
    private String tableName;

    /**
     * 子表描述
     */
    private String tableDescription = "描述";

    /**
     * 子表间隔行数
     */
    private Integer relativeHeadRowIndex = 1;

    /**
     * 表头所占行数
     */
    private Integer headRows = 2;

    /**
     * 子表行数据修改扩展
     */
    private TableRowDataReadHandler<T> rowDataReadHandler;

    /**
     * 子表数据修改扩展
     */
    private TableDataReadHandler<T> tableDataReadHandler;
    /**
     * 数据
     */
    private List<T> data;

    /**
     * 校验失败行
     */
    private List<ValidateFailRow<T>> validateFailRows;

    public ExcelReadTableVO(Integer sheetIndex, Integer tableIndex, Class<T> clazz) {
        this.sheetIndex = sheetIndex;
        this.tableIndex = tableIndex;
        this.clazz = clazz;
    }

    public ExcelReadTableVO(Integer sheetIndex, Integer tableIndex, Class<T> clazz,
                            TableRowDataReadHandler<T> rowDataReadHandler) {
        this.sheetIndex = sheetIndex;
        this.tableIndex = tableIndex;
        this.clazz = clazz;
        this.rowDataReadHandler = rowDataReadHandler;
    }

    public ExcelReadTableVO(Integer sheetIndex, Module module, Integer tableIndex, SubModule subModule,
                            Class<T> clazz) {
        this(sheetIndex, module, tableIndex, subModule, clazz, null);
    }

    public ExcelReadTableVO(Integer sheetIndex, Module module, Integer tableIndex, SubModule subModule, Class<T> clazz,
                            TableRowDataReadHandler<T> rowDataReadHandler) {
        this(sheetIndex,
             null,
             null,
             module.getCode(),
             module.getMsg(),
             tableIndex,
             subModule.getCode(),
             subModule.getMsg(),
             clazz,
             rowDataReadHandler);
    }

    public ExcelReadTableVO(Integer sheetIndex, String sheetCode, String sheetName, Integer tableIndex,
                            String tableCode, String tableName, Class<T> clazz,
                            TableRowDataReadHandler<T> rowDataReadHandler) {
        this(sheetIndex, sheetCode, sheetName, null, null, tableIndex, tableCode, tableName, clazz, rowDataReadHandler);
    }

    public ExcelReadTableVO(Integer sheetIndex, String sheetCode, String sheetName, String tableModuleCode,
                            String tableModuleName, Integer tableIndex, String tableCode, String tableName,
                            Class<T> clazz, TableRowDataReadHandler<T> rowDataReadHandler) {
        this.sheetIndex = sheetIndex;
        this.sheetCode = sheetCode;
        this.sheetName = sheetName;
        this.tableModuleCode = tableModuleCode;
        this.tableModuleName = tableModuleName;
        this.tableIndex = tableIndex;
        this.tableCode = tableCode;
        this.tableName = tableName;
        this.clazz = clazz;
        this.rowDataReadHandler = rowDataReadHandler;
    }

    private ExcelReadTableVO() {}

    /**
     * 构建子表解析结果
     *
     * @param data
     * @param validateFailRows
     */
    public void buildParsedResult(List<T> data, List<ValidateFailRow<T>> validateFailRows) {
        setData(data);
        setValidateFailRows(validateFailRows);
    }

    public void read(ExcelSheetReadContext context) {
        doRead(context);
    }

    public void doRead(ExcelSheetReadContext sheetReadContext) {
        ExcelReaderBuilder readerBuilder = null;
        if (Objects.nonNull(sheetReadContext.getSourceByteArray())) {//基于输入流
            //Easy excel会自动关闭输入流，不用特意关闭
            InputStream inputStream = sheetReadContext.getCloneInputStream();
            readerBuilder = EasyExcel.read(inputStream, getClazz(), new RuleCheckListener<>(sheetReadContext));
        } else {
            readerBuilder = EasyExcel.read(sheetReadContext.getFileName(),
                                           getClazz(),
                                           new RuleCheckListener<>(sheetReadContext));
        }
        try {
            readerBuilder.ignoreEmptyRow(false)
                         .sheet(this.getSheetIndex())
                         .headRowNumber(sheetReadContext.getNextTableHeadRowIndex())
                         .doRead();
        } catch (Exception e) {
            log.error("解析Excel失败，sheetName={},tableName={},子表对应模型Class={},子表间隔行数={},表头所占行数={}",
                      sheetReadContext.getSheetName(),
                      tableName,
                      clazz.getSimpleName(),
                      relativeHeadRowIndex,
                      headRows,
                      e);
            throw new RuntimeException("基于输入流解析Excel失败");
        }

    }

}