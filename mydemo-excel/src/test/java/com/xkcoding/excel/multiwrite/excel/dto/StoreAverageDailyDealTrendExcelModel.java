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
 * 一心便利>店日均交易次数趋势
 *
 * @Author WuHuaQiang
 * @Date 2024/03/14 20:35
 **/

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreAverageDailyDealTrendExcelModel extends ValidateErrorHead {
    @ExcelProperty(index = 0, value = {"一心便利>店日均交易次数趋势", "日期（yy/mm)"})
    @NotBlank(message = "日期（yy/mm)不能为空")
    private String key;

    @ExcelProperty(index = 1, value = {"一心便利>店日均交易次数趋势", "日均交易(次)"})
    @NotBlank(message = "日均交易不能为空")
    @Pattern(regexp = PERCENT, message = "日均交易请填写数字")
    private String valueStr;

    @ExcelIgnore
    private BigDecimal value;

    public void initBigDecimal() {
        value = Str2Decimal.convertValue(valueStr);
    }

}
