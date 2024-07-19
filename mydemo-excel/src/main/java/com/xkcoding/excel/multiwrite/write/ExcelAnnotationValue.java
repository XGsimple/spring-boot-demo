package com.xkcoding.excel.multiwrite.write;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ExcelAnnotationValue {

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * ExcelProperty注解 属性value数组
     */
    private String[] values;
}
