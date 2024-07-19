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
 * 一心便利>零售进销差完成率
 *
 * @Author WuHuaQiang
 * @Date 2024/03/14 20:32
 **/

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetailSaleDifferenceCompletionRateExcelModel extends ValidateErrorHead {
    @ExcelProperty(index = 0, value = {"一心便利>零售进销差完成率", "零售进销差完成率(%)"})
    @NotBlank(message = "零售进销差完成率不能为空")
    @Pattern(regexp = PERCENT, message = "零售进销差完成率请填写数字")
    private String valueStr;

    @ExcelIgnore
    private BigDecimal value;

    @ExcelIgnore
    private String unit;

    @ExcelProperty(index = 1, value = {"一心便利>零售进销差完成率", "进销差同比(%)"})
    @NotBlank(message = "进销差同比不能为空")
    @Pattern(regexp = PERCENT, message = "进销差同比请填写数字")
    private String yoyValueStr;

    @ExcelIgnore
    private BigDecimal yoyValue;

    @ExcelIgnore
    private String yoyUnit;

    @ExcelProperty(index = 2, value = {"一心便利>零售进销差完成率", "进销差环比(%)"})
    @NotBlank(message = "进销差不能为空")
    @Pattern(regexp = PERCENT, message = "进销差请填写数字")
    private String popValueStr;

    @ExcelIgnore
    private BigDecimal popValue;

    @ExcelIgnore
    private String popUnit;

    public void initBigDecimal() {
        value = Str2Decimal.convertValue(valueStr);
        yoyValue = Str2Decimal.convertValue(yoyValueStr);
        popValue = Str2Decimal.convertValue(popValueStr);
    }

}
