package com.xkcoding.excel.multiwrite.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.xkcoding.excel.SpringBootApplicationTests;
import com.xkcoding.excel.multiwrite.dto.DemoData1ExcelReadTableVO;
import com.xkcoding.excel.multiwrite.dto.ExcelReadTableVO;
import com.xkcoding.excel.multiwrite.dto.ExcelSheetReadContext;
import com.xkcoding.excel.multiwrite.dto.ValidateFailRow;
import com.xkcoding.excel.multiwrite.enums.Module;
import com.xkcoding.excel.multiwrite.enums.SubModule;
import com.xkcoding.excel.multiwrite.excel.dto.DemoSheet1Data;
import com.xkcoding.excel.multiwrite.excel.dto.DemoSheet2Data;
import com.xkcoding.excel.multiwrite.excel.dto.DemoSheet3Data;
import com.xkcoding.excel.multiwrite.excel.readlistener.DemoSheet1ReadListener;
import com.xkcoding.excel.multiwrite.excel.readlistener.DemoSheet2ReadListener;
import com.xkcoding.excel.multiwrite.excel.utils.MyExcelUtils;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author XuGang
 * @Date 2024/3/1 10:14
 */
@Slf4j
public class MultiSheetsReadTest extends SpringBootApplicationTests {
    @Test
    @Ignore
    public void newRepeatedRead() throws FileNotFoundException {
        String fileName = MultiSheetsReadTest.class.getResource("/").getPath() + "DiffHeadSheets.xlsx";

        List<ValidateFailRow> errors = new ArrayList<>();
        // 写法1
        try (ExcelReader excelReader = EasyExcel.read(fileName).build()) {
            // 这里为了简单 所以注册了 同样的head 和Listener 自己使用功能必须不同的Listener
            ReadSheet readSheet1 = EasyExcel.readSheet(0)
                                            .head(DemoSheet1Data.class)
                                            .registerReadListener(new DemoSheet1ReadListener())
                                            .build();
            // 这里注意 一定要把sheet1 sheet2 一起传进去，不然有个问题就是03版的excel 会读取多次，浪费性能
            excelReader.read(readSheet1);
            log.info("错误={}", errors);
        }
    }

    @Test
    @Ignore
    public void repeatedRead() {
        String fileName = MultiSheetsReadTest.class.getResource("/").getPath() + "DiffHeadSheets.xlsx";
        // 写法1
        try (ExcelReader excelReader = EasyExcel.read(fileName).build()) {
            // 这里为了简单 所以注册了 同样的head 和Listener 自己使用功能必须不同的Listener
            ReadSheet readSheet1 = EasyExcel.readSheet(0)
                                            .head(DemoSheet1Data.class)
                                            .registerReadListener(new DemoSheet1ReadListener())
                                            .build();
            ReadSheet readSheet2 = EasyExcel.readSheet(1)
                                            .head(DemoSheet2Data.class)
                                            .registerReadListener(new DemoSheet2ReadListener())
                                            .build();
            // 这里注意 一定要把sheet1 sheet2 一起传进去，不然有个问题就是03版的excel 会读取多次，浪费性能
            excelReader.read(readSheet1, readSheet2);
        }
    }

    @Test
    @Ignore
    public void repeatedReadAndValid() {
        String fileName = MultiSheetsReadTest.class.getResource("/").getPath() + "DiffHeadSheets.xlsx";
        // 写法1
        try (ExcelReader excelReader = EasyExcel.read(fileName).build()) {
            // 这里为了简单 所以注册了 同样的head 和Listener 自己使用功能必须不同的Listener
            ReadSheet readSheet1 = EasyExcel.readSheet(0)
                                            .head(DemoSheet1Data.class)
                                            .registerReadListener(new DemoSheet1ReadListener(true))
                                            .build();
            ReadSheet readSheet2 = EasyExcel.readSheet(1)
                                            .head(DemoSheet2Data.class)
                                            .registerReadListener(new DemoSheet2ReadListener())
                                            .build();
            // 这里注意 一定要把sheet1 sheet2 一起传进去，不然有个问题就是03版的excel 会读取多次，浪费性能
            excelReader.read(readSheet1, readSheet2);
        }
    }

    /**
     * 单sheet页，多表格读取
     */
    @Test
    @Ignore
    public void singleSheetMultiTableReadByUtil() {
        String fileName = MultiSheetsReadTest.class.getResource("/").getPath() + "SingleSheetMultiTable.xlsx";
        ExcelSheetReadContext sheet0ReadContext = buildSheet0(fileName);
        ExcelSheetReadContext sheet1ReadContext = buildSheet1(fileName);
        //解析sheet页所有子表
        MyExcelUtils.readSheets(sheet0ReadContext, sheet1ReadContext);
        sheet0ReadContext.getExcelReadTables().forEach(excelReadTable -> {
        });
        log.info("解析完成");
    }

    /**
     * 多sheet页多table，从文件中读取
     *
     * @throws IOException
     */
    @Test
    @Ignore
    public void multiSheetMultiTableReadFromFile() throws IOException {
        String fileName = MultiSheetsReadTest.class.getResource("/").getPath() + "SingleSheetMultiTable.xlsx";
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(fileName));
        byte[] byteArray = IOUtils.toByteArray(inputStream);
        ExcelSheetReadContext sheet0ReadContext = buildSheet0(fileName);
        ExcelSheetReadContext sheet1ReadContext = buildSheet1(fileName);
        sheet0ReadContext.setSourceByteArray(byteArray);
        sheet1ReadContext.setSourceByteArray(byteArray);
        //解析sheet页所有子表
        MyExcelUtils.readSheets(sheet0ReadContext, sheet1ReadContext);
        sheet0ReadContext.getExcelReadTables().forEach(excelReadTable -> {
        });
        log.info("解析完成");
    }

    public ExcelSheetReadContext buildSheet0(String fileName) {
        ExcelSheetReadContext sheetReadContext = new ExcelSheetReadContext();
        sheetReadContext.setSheetIndex(0);
        sheetReadContext.setFileName(fileName);
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
            excelReadTable.setRelativeHeadRowIndex(1);
            excelReadTable.setHeadRows(1);
        });
        return sheetReadContext;
    }

    public ExcelSheetReadContext buildSheet1(String fileName) {
        ExcelSheetReadContext sheetReadContext = new ExcelSheetReadContext();
        sheetReadContext.setSheetIndex(1);
        sheetReadContext.setFileName(fileName);
        //子表顺序
        int order = 0;
        ExcelReadTableVO<DemoSheet1Data> table1PointDTO = new DemoData1ExcelReadTableVO<DemoSheet1Data>(1,
                                                                                                        Module.TEST,
                                                                                                        order++,
                                                                                                        SubModule.TEST,
                                                                                                        DemoSheet1Data.class);
        ExcelReadTableVO<DemoSheet2Data> table2PointDTO = new ExcelReadTableVO<DemoSheet2Data>(1,
                                                                                               Module.TEST,
                                                                                               order++,
                                                                                               SubModule.TEST,
                                                                                               DemoSheet2Data.class);
        sheetReadContext.addExcelReadTable(table1PointDTO);
        sheetReadContext.addExcelReadTable(table2PointDTO);
        sheetReadContext.getExcelReadTables().forEach(excelReadTable -> {
            excelReadTable.setRelativeHeadRowIndex(1);
            excelReadTable.setHeadRows(1);
        });
        return sheetReadContext;
    }
}