package com.xkcoding.excel.exceltool.dropdown.annotation;

import com.xkcoding.excel.exceltool.dropdown.enums.ChainDropDownType;

import java.lang.annotation.*;

/**
 * excel的级联下拉框注解
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChainDropDownFields {

    /**
     * 动态下拉内容，可查数据库返回等，其他操作
     *
     * @return class[]
     */
    Class[] sourceClass() default {};

    /**
     * 是不是第一级下拉
     *
     * @return
     */
    boolean isRoot() default false;

    /**
     * 获取下拉的参数,可以有1个到多个
     *
     * @return string
     */
    String[] params() default {};

    /**
     * 级联下拉类型
     *
     * @return type
     */
    ChainDropDownType type() default ChainDropDownType.NONE;

    /**
     * 设置下拉框的起始行，默认为第二行
     */
    int firstRow() default 1;

    /**
     * 设置下拉框的结束行，默认为最后一行
     */
    int lastRow() default 0x10000;

}
