package com.xkcoding.excel.multiwrite.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author XuGang
 * @Date 2024/3/6 17:53
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class ExcelSheetReadContext {

    private Integer sheetIndex;

    private String sheetName;

    private String fileName;

    private byte[] sourceByteArray;

    /**
     * 当前正在解析的表
     */
    private ExcelReadTableVO currentReadTable;

    /**
     * 当前正在解析的表的索引
     */
    private Integer currentReadTableIndex = -1;

    /**
     * 该sheet中的子表
     */
    private List<ExcelReadTableVO> excelReadTables = new ArrayList<>();

    /**
     * 是否有校验错误
     */
    private Boolean hasValidateError = Boolean.FALSE;

    /**
     * 已解析的行数
     */
    private int parsedRows = 0;

    /**
     * 下一个表头行索引
     */
    private Integer nextTableHeadRowIndex = 0;

    public ExcelSheetReadContext(Integer sheetIndex, String sheetName) {
        this.sheetIndex = sheetIndex;
        this.sheetName = sheetName;
    }

    /**
     * 主流程，读取子表
     */
    public void read() {
        try {
            getExcelReadTables().forEach(table -> {
                incrementCurrentReadTableIndex();
                setCurrentReadTable(table);
                updateNextTableHeadRowIndex();
                log.info("开始读取指定sheet中的子表数据，sheetName={},tableName={},currentReadTableIndex={},nextTableHeadRowIndex={}",
                         sheetName,
                         table.getTableName(),
                         currentReadTableIndex,
                         getNextTableHeadRowIndex());
                table.read(this);
            });
        } catch (Exception e) {
            log.error("读取指定sheet中的子表数据报错，sheetIndex={},sheetName={},currentReadTableIndex={}",
                      sheetIndex,
                      sheetName,
                      currentReadTableIndex);
            throw new RuntimeException("读取指定sheet中的子表数据报错");
        }
    }

    public void updateRowIndex(int parsedRows) {
        this.parsedRows += parsedRows;
        this.nextTableHeadRowIndex += parsedRows;
    }

    public void incrementCurrentReadTableIndex() {
        currentReadTableIndex++;
    }

    public void updateNextTableHeadRowIndex() {
        int nextReadTableIndex = currentReadTableIndex + 1;
        if (nextReadTableIndex > excelReadTables.size()) {
            throw new RuntimeException("没有下一张表");
        }
        Integer relativeHeadRowIndex = currentReadTable.getRelativeHeadRowIndex();
        Integer currentHeadRows = currentReadTable.getHeadRows();
        this.nextTableHeadRowIndex += currentHeadRows;
        if (currentReadTableIndex != 0) {
            this.nextTableHeadRowIndex += relativeHeadRowIndex;
        }
    }

    public <T> void addExcelReadTable(ExcelReadTableVO<T> tablePointDTO) {
        this.getExcelReadTables().add(tablePointDTO);
    }

    /**
     * 根据原字节数组，生成克隆输入流
     *
     * @return
     */
    public InputStream getCloneInputStream() {
        return new ByteArrayInputStream(this.sourceByteArray);
    }
}