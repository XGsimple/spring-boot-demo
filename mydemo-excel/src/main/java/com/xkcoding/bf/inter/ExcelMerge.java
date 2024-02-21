package com.xkcoding.bf.inter;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelMerge {
    /**
     * 是否合并单元格
     *
     * @return true || false
     */
    boolean merge() default true;

    /**
     * 是否为主键（即该字段相同的行合并）
     *
     * @return true || false
     */
    boolean isPrimaryKey() default false;
}
