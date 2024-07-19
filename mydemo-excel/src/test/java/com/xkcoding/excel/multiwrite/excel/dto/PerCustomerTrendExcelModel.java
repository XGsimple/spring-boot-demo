package com.xkcoding.excel.multiwrite.excel.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.yxt.bigdata.decision.server.monthly.common.Str2Decimal;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

import static com.yxt.bigdata.decision.server.monthly.common.Regexp.PERCENT;

/**
 * 一心便利>客单价趋势
 *
 * @Author WuHuaQiang
 * @Date 2024/03/14 20:36
 **/

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerCustomerTrendExcelModel extends ValidateErrorHead {
    @ExcelProperty(index = 0, value = {"一心便利>客单价趋势", "日期（yy/mm)"})
    @NotBlank(message = "日期（yy/mm)不能为空")
    private String key;

    @ExcelProperty(index = 1, value = {"一心便利>客单价趋势", "客单价(元)"})
    @NotBlank(message = "客单价不能为空")
    @Pattern(regexp = PERCENT, message = "客单价请填写数字")
    private String valueStr;

    @ExcelIgnore
    private BigDecimal value;

    public void initBigDecimal() {
        value = Str2Decimal.convertValue(valueStr);
    }

}
