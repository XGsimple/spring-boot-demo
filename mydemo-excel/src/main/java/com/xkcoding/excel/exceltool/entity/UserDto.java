package com.xkcoding.excel.exceltool.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.xkcoding.excel.exceltool.converter.LocalDateTimeConverter;
import com.xkcoding.excel.exceltool.converter.SexConverter;
import com.xkcoding.excel.exceltool.dropdown.annotation.DropDownFields;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * ColumnWidth 定义列宽
 */
@ColumnWidth(25)
@Data
@Accessors(chain = true)
public class UserDto {

    @ExcelProperty("用户名称")
    private String username;

    @ExcelProperty("年龄")
    private Integer age;

    @ExcelProperty(value = "性别", converter = SexConverter.class)
    private Integer sex;

    @ExcelProperty("部门")
    @DropDownFields(source = {"财务部", "人事部", "研发部", "商务部"})
    private String department;

    @ExcelProperty("职业")
    private String occupation;

    /**
     * 自定义日期转换器 LocalDateTimeConverter
     */
    @ColumnWidth(50)
    @ExcelProperty(value = "注册时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime createTime;

    /**
     * 使用注解 @DateTimeFormat
     */
    @ExcelProperty(value = "发财时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ColumnWidth(50)
    private Date startTime;
}
