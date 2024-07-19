package com.xkcoding.excel.multiwrite.dto;

import com.xkcoding.excel.multiwrite.enums.Module;
import com.xkcoding.excel.multiwrite.enums.SubModule;
import com.xkcoding.excel.multiwrite.handler.TableRowDataReadHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author XuGang
 * @Date 2024/3/7 13:53
 */
@Slf4j
@Setter
@Getter
@Accessors(chain = true)
public class DemoData1ExcelReadTableVO<DemoSheet1Data> extends ExcelReadTableVO<DemoSheet1Data> {

    public DemoData1ExcelReadTableVO(Integer sheetIndex, Integer tableIndex, Class<DemoSheet1Data> clazz) {
        super(sheetIndex, tableIndex, clazz);
    }

    public DemoData1ExcelReadTableVO(Integer sheetIndex, Integer tableIndex, Class<DemoSheet1Data> clazz,
                                     TableRowDataReadHandler<DemoSheet1Data> rowDataReadHandler) {
        super(sheetIndex, tableIndex, clazz, rowDataReadHandler);
    }

    public DemoData1ExcelReadTableVO(Integer sheetIndex, Module module, Integer tableIndex, SubModule subModule,
                                     Class<DemoSheet1Data> clazz) {
        super(sheetIndex, module, tableIndex, subModule, clazz);
    }

    public DemoData1ExcelReadTableVO(Integer sheetIndex, Module module, Integer tableIndex, SubModule subModule,
                                     Class<DemoSheet1Data> clazz,
                                     TableRowDataReadHandler<DemoSheet1Data> rowDataReadHandler) {
        super(sheetIndex, module, tableIndex, subModule, clazz, rowDataReadHandler);
    }

    public DemoData1ExcelReadTableVO(Integer sheetIndex, String sheetCode, String sheetName, Integer tableIndex,
                                     String tableCode, String tableName, Class<DemoSheet1Data> clazz,
                                     TableRowDataReadHandler<DemoSheet1Data> rowDataReadHandler) {
        super(sheetIndex, sheetCode, sheetName, tableIndex, tableCode, tableName, clazz, rowDataReadHandler);
    }

    public DemoData1ExcelReadTableVO(Integer sheetIndex, String sheetCode, String sheetName, String tableModuleCode,
                                     String tableModuleName, Integer tableIndex, String tableCode, String tableName,
                                     Class<DemoSheet1Data> clazz,
                                     TableRowDataReadHandler<DemoSheet1Data> rowDataReadHandler) {
        super(sheetIndex,
              sheetCode,
              sheetName,
              tableModuleCode,
              tableModuleName,
              tableIndex,
              tableCode,
              tableName,
              clazz,
              rowDataReadHandler);
    }

}