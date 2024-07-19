package com.xkcoding.excel.multiwrite.excel.utils;

import java.math.BigDecimal;

/**
 * @author ZhangCheng
 * @date 2024/04/17
 **/
public class Str2Decimal {

    public static BigDecimal convertValue(String value) {
        try {
            if (value.contains("%")) {
                value = value.replace("%", "");
            }
            return new BigDecimal(value);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
