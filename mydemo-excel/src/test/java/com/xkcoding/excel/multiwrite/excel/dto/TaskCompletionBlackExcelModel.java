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
 * 大型活动>任务达成黑榜-分部
 *
 * @Author WuHuaQiang
 * @Date 2024/03/15 10:02
 **/

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskCompletionBlackExcelModel extends ValidateErrorHead {
    @ExcelProperty(index = 0, value = {"大型活动>任务达成黑榜-分部", "分部名称"})
    @NotBlank(message = "分部名称不能为空")
    private String key;

    @ExcelProperty(index = 1, value = {"大型活动>任务达成黑榜-分部", "任务达成率(%)"})
    @NotBlank(message = "任务达成率不能为空")
    @Pattern(regexp = PERCENT, message = "任务达成率请填写数字")
    private String taskCompletionRateStr;

    @ExcelIgnore
    private BigDecimal taskCompletionRate;

    @ExcelIgnore
    private String taskCompletionRateUnit;

    @ExcelProperty(index = 2, value = {"大型活动>任务达成黑榜-分部", "排名"})
    @NotBlank(message = "排名不能为空")
    @Pattern(regexp = PERCENT, message = "排名请填写数字")
    private String orderStr;

    @ExcelIgnore
    private BigDecimal order;

    public void initBigDecimal() {
        taskCompletionRate = Str2Decimal.convertValue(taskCompletionRateStr);
        order = Str2Decimal.convertValue(orderStr);
    }

}
