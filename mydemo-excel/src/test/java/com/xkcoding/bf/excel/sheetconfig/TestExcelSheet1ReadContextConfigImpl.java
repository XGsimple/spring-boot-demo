package com.xkcoding.bf.excel.sheetconfig;

import com.xkcoding.bf.dto.DemoData1ExcelReadTableVO;
import com.xkcoding.bf.dto.ExcelReadTableVO;
import com.xkcoding.bf.dto.ExcelSheetReadContext;
import com.xkcoding.bf.enums.ImportFileTemplateEnum;
import com.xkcoding.bf.enums.Module;
import com.xkcoding.bf.enums.SubModule;
import com.xkcoding.bf.excel.dto.DemoSheet1Data;
import com.xkcoding.bf.excel.dto.DemoSheet2Data;
import com.xkcoding.bf.sheetconfig.read.ExcelSheetReadContextConfig;
import org.springframework.stereotype.Component;

/**
 * excel sheet 配置
 *
 * @Author XuGang
 * @Date 2024/3/11 13:41
 */
@Component
public class TestExcelSheet1ReadContextConfigImpl implements ExcelSheetReadContextConfig {
    @Override
    public ImportFileTemplateEnum getTemplateEnum() {
        return ImportFileTemplateEnum.TEST;
    }

    @Override
    public Integer getSheetIndex() {
        return 1;
    }

    @Override
    public ExcelSheetReadContext configExcelSheetReadContext(byte[] byteArray) {
        return buildSheet(byteArray);
    }

    public ExcelSheetReadContext buildSheet(byte[] buffer) {
        ExcelSheetReadContext sheetReadContext = new ExcelSheetReadContext(getSheetIndex(), "sheet1");
        sheetReadContext.setSheetIndex(getSheetIndex());
        sheetReadContext.setSourceByteArray(buffer);
        //子表顺序
        int order = 0;
        ExcelReadTableVO<DemoSheet1Data> table1PointDTO = new DemoData1ExcelReadTableVO<DemoSheet1Data>(getSheetIndex(),
                                                                                                        Module.TEST,
                                                                                                        order++,
                                                                                                        SubModule.TEST,
                                                                                                        DemoSheet1Data.class,
                                                                                                        theRowData -> {
                                                                                                            theRowData.setString(
                                                                                                                "修改");
                                                                                                        });
        ExcelReadTableVO<DemoSheet2Data> table2PointDTO = new ExcelReadTableVO<DemoSheet2Data>(getSheetIndex(),
                                                                                               Module.TEST,
                                                                                               order++,
                                                                                               SubModule.TEST,
                                                                                               DemoSheet2Data.class);

        sheetReadContext.addExcelReadTable(table1PointDTO);
        sheetReadContext.addExcelReadTable(table2PointDTO);
        sheetReadContext.getExcelReadTables().forEach(excelReadTable -> {
            excelReadTable.setRelativeHeadRowIndex(2);
            excelReadTable.setHeadRows(1);
            excelReadTable.setTableDescription("sheet1");
        });
        return sheetReadContext;
    }
}