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
 * 大型活动>任务达成-子公司
 *
 * @Author WuHuaQiang
 * @Date 2024/03/15 09:55
 **/
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubTaskCompletionExcelModel extends ValidateErrorHead {

    @ExcelProperty(index = 0, value = {"大型活动>任务达成-子公司", "公司名称"})
    @NotBlank(message = "公司名称不能为空")
    private String key;

    @ExcelProperty(index = 1, value = {"大型活动>任务达成-子公司", "任务达成率(%)"})
    @NotBlank(message = "任务达成率不能为空")
    @Pattern(regexp = PERCENT, message = "任务达成率请填写数字")
    private String taskCompletionRateStr;

    @ExcelIgnore
    private BigDecimal taskCompletionRate;

    @ExcelIgnore
    private String taskCompletionRateUnit;

    @ExcelProperty(index = 2, value = {"大型活动>任务达成-子公司", "达标线(%)"})
    @NotBlank(message = "达标线不能为空")
    @Pattern(regexp = PERCENT, message = "达标线请填写数字")
    private String targetValueStr;

    @ExcelIgnore
    private BigDecimal targetValue;

    @ExcelIgnore
    private String targetUnit;

    public void initBigDecimal() {
        taskCompletionRate = Str2Decimal.convertValue(taskCompletionRateStr);
        targetValue = Str2Decimal.convertValue(targetValueStr);
    }
}
