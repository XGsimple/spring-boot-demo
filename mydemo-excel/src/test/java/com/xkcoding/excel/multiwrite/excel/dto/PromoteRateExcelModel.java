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
 * 大型活动>实际提升率
 *
 * @Author WuHuaQiang
 * @Date 2024/03/15 09:48
 **/
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoteRateExcelModel extends ValidateErrorHead {

    @ExcelProperty(index = 0, value = {"大型活动>实际提升率", "实际提升率(%)"})
    @NotBlank(message = "实际提升率不能为空")
    @Pattern(regexp = PERCENT, message = "实际提升率请填写数字")
    private String valueStr;

    @ExcelIgnore
    private BigDecimal value;

    @ExcelIgnore
    private String unit;

    public void initBigDecimal() {
        value = Str2Decimal.convertValue(valueStr);
    }

}