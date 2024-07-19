package com.xkcoding.excel.exceltool.dropdown.annotation;

import com.xkcoding.excel.exceltool.dropdown.enums.DropDownType;

import java.lang.annotation.*;

/**
 * 导出excel的下拉框注解
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DropDownFields {
    /**
     * 固定下拉
     *
     * @return
     */
    String[] source() default {};

    /**
     * 动态下拉内容，可查数据库返回等，其他操作
     *
     * @return
     */
    Class[] sourceClass() default {};

    /**
     * 下拉类型，可能动态查询的时候需要用到
     *
     * @return
     */
    DropDownType type() default DropDownType.NONE;

    /**
     * 设置下拉框的起始行，默认为第二行
     */
    int firstRow() default 1;

    /**
     * 设置下拉框的结束行，默认为最后一行
     */
    int lastRow() default 0x10000;
}
