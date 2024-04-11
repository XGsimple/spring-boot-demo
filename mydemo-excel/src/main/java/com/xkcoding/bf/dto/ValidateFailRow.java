package com.xkcoding.bf.dto;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class ValidateFailRow<T> {

    /**
     * 不符合数据校验规则的所在sheet页中行位置
     */
    private Integer rowIndex;

    /**
     * 不符合数据校验规则的所在子表中行位置
     */
    private Integer tableRowIndex;

    private List<CheckFailColumn> checkFailColumns;

    private T errorData;

    public ValidateFailRow(Integer rowIndex, Integer tableRowIndex) {
        this(rowIndex, tableRowIndex, 100);

    }

    public ValidateFailRow(Integer rowIndex, Integer tableRowIndex, Integer columnSize) {
        this.rowIndex = rowIndex;
        this.tableRowIndex = tableRowIndex;
        this.checkFailColumns = new ArrayList<>(columnSize);
    }

    public String getErrorMsg() {
        return checkFailColumns.stream()
                               .map(CheckFailColumn::getMessage)
                               .filter(StrUtil::isNotBlank)
                               .collect(Collectors.joining(";"));
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @Accessors(chain = true)
    @ToString
    public static class CheckFailColumn {
        /**
         * 不符合数据校验规则的列所在位置
         */
        private Integer columnIndex;

        /**
         * 不符合数据校验规则的提示信息
         */
        private String message;

        /**
         * 不符合数据校验规则的属性值
         */
        private Object propertyValue;

    }

}