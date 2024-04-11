package com.xkcoding.bf.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.xkcoding.bf.dto.ExportErrorFileResultDTO;
import com.xkcoding.bf.excel.dto.ComplexSubjectEasyExcel;
import com.xkcoding.bf.excel.dto.DemoSheet1Data;
import com.xkcoding.bf.excel.dto.DemoSheet2Data;
import com.xkcoding.bf.excel.utils.ValidatorUtil;
import com.xkcoding.bf.write.CustomCellWriteHandler;
import com.xkcoding.bf.write.EasyExcelWriteUtils;
import com.xkcoding.bf.write.ExcelSheetWriteContextVO;
import com.xkcoding.bf.write.ExcelWriteTableVO;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author XuGang
 * @Date 2024/3/1 10:14
 */
@Slf4j
public class MultiSheetsWriteTest {

    @Test
    @Ignore
    public void dynamicHeadWrite() {
        String writeFileName = MultiSheetsWriteTest.class.getResource("/").getPath() + "dynamicHeadValidateWrite" +
                               System.currentTimeMillis() + ".xlsx";

        // 这里 指定文件
        try (ExcelWriter excelWriter = EasyExcel.write(writeFileName).build()) {
            // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来。这里最终会写到5个sheet里面
            for (int i = 0; i < 2; i++) {
                WriteSheet writeSheet = null;
                switch (i) {
                    case 0:
                        writeSheet = EasyExcel.writerSheet(i, "模板" + i).head(DemoSheet1Data.class).build();
                        excelWriter.write(data1(), writeSheet);
                        break;
                    case 1:
                        writeSheet = EasyExcel.writerSheet(i, "模板" + i).head(DemoSheet1Data.class).build();
                        excelWriter.write(data2(), writeSheet);
                        break;
                }
            }
        }
    }

    @Test
    @Ignore
    public void moreSheetMoreTableTest() {
        ComplexSubjectEasyExcel tableData = new ComplexSubjectEasyExcel().setSubjectId("1001")
                                                                         .setSubjectName("库存现金")
                                                                         .setFirstBorrowMoney(BigDecimal.valueOf(100))
                                                                         .setNowBorrowMoney(BigDecimal.valueOf(105))
                                                                         .setNowCreditMoney(BigDecimal.valueOf(100))
                                                                         .setYearBorrowMoney(BigDecimal.valueOf(200))
                                                                         .setYearCreditMoney(BigDecimal.valueOf(205))
                                                                         .setEndBorrowMoney(BigDecimal.valueOf(240));
        List<ComplexSubjectEasyExcel> tableDataList = new ArrayList<>();
        tableDataList.add(tableData);

        List<ExcelSheetWriteContextVO> sheets = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            ExcelSheetWriteContextVO theSheet = new ExcelSheetWriteContextVO().setSheetName("科目余额表" + i)
                                                                              .setHandlers(Collections.singletonList(new CustomCellWriteHandler()));
            List<ExcelWriteTableVO> tables = new ArrayList<>();
            ExcelWriteTableVO table = new ExcelWriteTableVO().setClazz(ComplexSubjectEasyExcel.class)
                                                             .setData(tableDataList);
            tables.add(table);
            if (i == 1) {
                tables.add(table);
            }
            theSheet.setTables(tables);
            sheets.add(theSheet);
        }
        ExportErrorFileResultDTO exportErrorFileResultDTO = EasyExcelWriteUtils.multiSheetMultiTableExportToLocal(sheets,
                                                                                                                  "",
                                                                                                                  "科目余额表-多sheet多表");
    }

    private List<DemoSheet1Data> data1() {
        List<DemoSheet1Data> list = ListUtils.newArrayList();
        for (int i = 0; i < 10; i++) {
            DemoSheet1Data data = new DemoSheet1Data();
            data.setString("字符串" + i);
            data.setDoubleData(0.56);
            Set<ConstraintViolation<Object>> validateSet = ValidatorUtil.validateAndReturnResults(data);
            String errorMsg = validateSet.stream()
                                         .map(ConstraintViolation::getMessage)
                                         .collect(Collectors.joining("&"));
            data.setValidateResult(errorMsg);
            list.add(data);
        }
        return list;
    }

    private List<DemoSheet2Data> data2() {
        List<DemoSheet2Data> list = ListUtils.newArrayList();
        for (int i = 0; i < 10; i++) {
            DemoSheet2Data data = new DemoSheet2Data();
            data.setString("字符串" + i);
            data.setDoubleData(0.56);
            Set<ConstraintViolation<Object>> validateSet = ValidatorUtil.validateAndReturnResults(data);
            String errorMsg = validateSet.stream()
                                         .map(ConstraintViolation::getMessage)
                                         .collect(Collectors.joining("&"));
            data.setValidateResult(errorMsg);
            list.add(data);
        }
        return list;
    }
}