package com.xkcoding.bf.excel.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.xkcoding.bf.dto.ValidateErrorHead;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * 基础数据类
 *
 * @author Jiaju Zhuang
 **/
@Getter
@Setter
public class DemoSheet1Data extends ValidateErrorHead {
    @ExcelProperty("字符串标题")
    @Pattern(regexp = "^[_a-zA-Z0-9]+$", message = "只允许英文、数字及下划线")
    private String string;

    @ExcelProperty("数字标题")
    @Max(value = 80, message = "必须小于80")
    @Min(value = 20, message = "必须大于20")
    private Double doubleData;

    /**
     * 忽略这个字段
     */
    @ExcelIgnore
    private String ignore;
}
