package com.xkcoding.excel.multiwrite.excel.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.xkcoding.excel.multiwrite.excel.utils.Str2Decimal;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

import static com.baomidou.mybatisplus.core.toolkit.StringPool.PERCENT;

/**
 * 大型活动>任务达成率，活动门店数
 *
 * @Author WuHuaQiang
 * @Date 2024/03/15 09:43
 **/

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskCompletionRateExcelModel extends ValidateErrorHead {

    @ExcelProperty(index = 0, value = {"大型活动>任务达成率，活动门店数", "任务达成率(%)"})
    @NotBlank(message = "任务达成率不能为空")
    @Pattern(regexp = PERCENT, message = "任务达成率请填写数字")
    private String taskCompletionRateStr;

    @ExcelIgnore
    private BigDecimal taskCompletionRate;

    @ExcelIgnore
    private String taskCompletionRateUnit;

    @ExcelProperty(index = 1, value = {"大型活动>任务达成率，活动门店数", "活动门店数"})
    @NotBlank(message = "活动门店数不能为空")
    @Pattern(regexp = PERCENT, message = "活动门店数请填写数字")
    private String activityStoreCountStr;

    @ExcelIgnore
    private BigDecimal activityStoreCount;

    public void initBigDecimal() {
        taskCompletionRate = Str2Decimal.convertValue(taskCompletionRateStr);
        activityStoreCount = Str2Decimal.convertValue(activityStoreCountStr);
    }
}
