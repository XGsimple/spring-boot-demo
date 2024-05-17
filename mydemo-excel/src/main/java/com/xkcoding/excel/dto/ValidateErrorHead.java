package com.xkcoding.bf.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 通用校验错误列头
 *
 * @Author XuGang
 * @Date 2024/3/13 18:08
 */
@Getter
@Setter
public class ValidateErrorHead {
    @HeadFontStyle(bold = BooleanEnum.TRUE)
    @HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 51)
    @ContentFontStyle(fontHeightInPoints = 12, fontName = "宋体", color = 10)
    @ExcelProperty(value = {"校验结果"})
    private String validateResult;
}