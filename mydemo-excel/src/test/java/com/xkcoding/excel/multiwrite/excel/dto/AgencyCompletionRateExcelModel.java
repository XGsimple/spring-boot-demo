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
 * 加盟店>签约完成率
 *
 * @Author WuHuaQiang
 * @Date 2024/03/14 20:16
 **/

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgencyCompletionRateExcelModel extends ValidateErrorHead {

    @ExcelProperty(index = 0, value = {"加盟店>签约完成率", "签约完成率(%)"})
    @NotBlank(message = "签约完成率")
    @Pattern(regexp = PERCENT, message = "签约完成率请填写数字")
    private String valueStr;

    @ExcelIgnore
    private String unit;
    @ExcelIgnore
    private BigDecimal value;

    public void initBigDecimal() {
        this.value = Str2Decimal.convertValue(this.valueStr);
    }
}
