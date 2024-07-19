package com.xkcoding.excel.multiwrite.excel.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.BorderStyleEnum;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@FieldNameConstants
@HeadRowHeight(value = 25)
@ContentRowHeight(value = 18)
@ColumnWidth(value = 20)
@HeadStyle(fillBackgroundColor = 64)
@HeadFontStyle(bold = BooleanEnum.FALSE)
@ContentStyle(borderTop = BorderStyleEnum.THIN,
              borderLeft = BorderStyleEnum.THIN,
              borderRight = BorderStyleEnum.THIN,
              borderBottom = BorderStyleEnum.THIN)
public class ComplexSubjectEasyExcel {

    @ExcelProperty(value = {"科目余额表", "编制单位：  测试单位321412", "科目编码", "科目编码"}, index = 0)
    private String subjectId;

    @ExcelProperty(value = {"科目余额表", "编制单位：  测试单位321412", "科目名称", "科目名称"}, index = 1)
    private String subjectName;

    @HeadFontStyle(bold = BooleanEnum.TRUE)
    @ExcelProperty(value = {"科目余额表", "编制单位：  测试单位321412", "期初余额", "借方"}, index = 2)
    private BigDecimal firstBorrowMoney;

    @HeadFontStyle(bold = BooleanEnum.TRUE)
    @ExcelProperty(value = {"科目余额表", "编制单位：  测试单位321412", "期初余额", "贷方"}, index = 3)
    private BigDecimal firstCreditMoney;

    @HeadFontStyle(bold = BooleanEnum.TRUE)
    @ExcelProperty(value = {"科目余额表", "2021年9月至2021年9月", "本期发生额", "借方"}, index = 4)
    private BigDecimal nowBorrowMoney;

    @HeadFontStyle(bold = BooleanEnum.TRUE)
    @ExcelProperty(value = {"科目余额表", "2021年9月至2021年9月", "本期发生额", "贷方"}, index = 5)
    private BigDecimal nowCreditMoney;

    @HeadFontStyle(bold = BooleanEnum.TRUE)
    @ExcelProperty(value = {"科目余额表", "2021年9月至2021年9月", "本年累计发生额", "借方"}, index = 6)
    private BigDecimal yearBorrowMoney;

    @HeadFontStyle(bold = BooleanEnum.TRUE)
    @ExcelProperty(value = {"科目余额表", "2021年9月至2021年9月", "本年累计发生额", "贷方"}, index = 7)
    private BigDecimal yearCreditMoney;

    @HeadFontStyle(bold = BooleanEnum.TRUE)
    @ExcelProperty(value = {"科目余额表", "单位：元", "期末余额", "借方"}, index = 8)
    private BigDecimal endBorrowMoney;

    @HeadFontStyle(bold = BooleanEnum.TRUE)
    @ExcelProperty(value = {"科目余额表", "单位：元", "期末余额", "贷方"}, index = 9)
    private BigDecimal endCreditMoney;

    @HeadFontStyle(bold = BooleanEnum.TRUE)
    @ExcelProperty(value = {"科目余额表", "校验信息", "校验信息", "校验信息"}, index = 10)
    private String validateError;

}
