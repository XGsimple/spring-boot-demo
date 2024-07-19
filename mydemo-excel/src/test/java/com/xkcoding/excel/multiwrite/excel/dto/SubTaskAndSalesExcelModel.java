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
 * 心耀商品-任务达成&进销差-子公司
 *
 * @Author WuHuaQiang
 * @Date 2024/03/14 19:33
 **/

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubTaskAndSalesExcelModel extends ValidateErrorHead {

    @ExcelProperty(index = 0, value = {"心耀商品-任务达成&进销差率-子公司", "公司名称"})
    @NotBlank(message = "公司名称不能为空")
    private String key;

    @ExcelProperty(index = 1, value = {"心耀商品-任务达成&进销差率-子公司", "任务达成(%)"})
    @NotBlank(message = "任务达成不能为空")
    @Pattern(regexp = PERCENT, message = "任务达成请填写数字")
    private String taskCompletionRateStr;

    @ExcelIgnore
    private String taskCompletionRateUnit;

    @ExcelProperty(index = 2, value = {"心耀商品-任务达成&进销差率-子公司", "进销差率(%)"})
    @NotBlank(message = "进销差率不能为空")
    @Pattern(regexp = PERCENT, message = "进销差率请填写数字")
    private String salesDifferenceRateStr;

    @ExcelIgnore
    private BigDecimal taskCompletionRate;
    @ExcelIgnore
    private BigDecimal salesDifferenceRate;

    @ExcelIgnore
    private String salesDifferenceRateUnit;

    public void initBigDecimal() {
        this.taskCompletionRate = Str2Decimal.convertValue(this.taskCompletionRateStr);
        this.salesDifferenceRate = Str2Decimal.convertValue(this.salesDifferenceRateStr);
    }

}
