package com.xkcoding.excel.multiwrite.excel.sheetconfig;

import com.xkcoding.excel.multiwrite.dto.DemoData1ExcelReadTableVO;
import com.xkcoding.excel.multiwrite.dto.ExcelReadTableVO;
import com.xkcoding.excel.multiwrite.dto.ExcelSheetReadContext;
import com.xkcoding.excel.multiwrite.enums.ImportFileTemplateEnum;
import com.xkcoding.excel.multiwrite.enums.Module;
import com.xkcoding.excel.multiwrite.enums.SubModule;
import com.xkcoding.excel.multiwrite.excel.dto.DemoSheet1Data;
import com.xkcoding.excel.multiwrite.excel.dto.DemoSheet2Data;
import com.xkcoding.excel.multiwrite.excel.dto.DemoSheet3Data;
import com.xkcoding.excel.multiwrite.sheetconfig.read.ExcelSheetReadContextConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * excel sheet 配置
 *
 * @Author XuGang
 * @Date 2024/3/11 13:41
 */
@Slf4j
@Component
public class TestExcelSheet0ReadContextConfigImpl implements ExcelSheetReadContextConfig {
    @Override
    public ImportFileTemplateEnum getTemplateEnum() {
        return ImportFileTemplateEnum.TEST;
    }

    @Override
    public Integer getSheetIndex() {
        return 0;
    }

    @Override
    public ExcelSheetReadContext configExcelSheetReadContext(byte[] byteArray) {
        return buildSheet(byteArray);
    }

    public ExcelSheetReadContext buildSheet(byte[] buffer) {
        ExcelSheetReadContext sheetReadContext = new ExcelSheetReadContext(getSheetIndex(), "sheet0");
        sheetReadContext.setSheetIndex(getSheetIndex());
        sheetReadContext.setSourceByteArray(buffer);
        //子表顺序
        int order = 0;
        ExcelReadTableVO<DemoSheet1Data> table1PointDTO = new DemoData1ExcelReadTableVO<DemoSheet1Data>(0,
                                                                                                        Module.TEST,
                                                                                                        order++,
                                                                                                        SubModule.TEST,
                                                                                                        DemoSheet1Data.class,
                                                                                                        theRowData -> {
                                                                                                            theRowData.setString(
                                                                                                                "修改");
                                                                                                        });
        table1PointDTO.setTableDataReadHandler((theTableDataList, errorList) -> {
            theTableDataList.stream()
                            .map(DemoSheet1Data::getDoubleData)
                            .reduce(Double::sum)
                            .ifPresent(sum -> log.warn("总和：{}", sum));
        });
        ExcelReadTableVO<DemoSheet2Data> table2PointDTO = new ExcelReadTableVO<DemoSheet2Data>(0,
                                                                                               Module.TEST,
                                                                                               order++,
                                                                                               SubModule.TEST,
                                                                                               DemoSheet2Data.class);
        ExcelReadTableVO<DemoSheet3Data> table3PointDTO = new ExcelReadTableVO<DemoSheet3Data>(0,
                                                                                               Module.TEST,
                                                                                               order++,
                                                                                               SubModule.TEST,
                                                                                               DemoSheet3Data.class);
        sheetReadContext.addExcelReadTable(table1PointDTO);
        sheetReadContext.addExcelReadTable(table2PointDTO);
        sheetReadContext.addExcelReadTable(table3PointDTO);
        sheetReadContext.getExcelReadTables().forEach(excelReadTable -> {
            excelReadTable.setRelativeHeadRowIndex(2);
            excelReadTable.setHeadRows(1);
            excelReadTable.setTableDescription("21");
        });
        return sheetReadContext;
    }
}