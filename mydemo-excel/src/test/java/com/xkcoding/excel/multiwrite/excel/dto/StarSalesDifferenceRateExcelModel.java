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
 * 心耀商品-进销差率
 *
 * @Author WuHuaQiang
 * @Date 2024/03/14 19:32
 **/
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StarSalesDifferenceRateExcelModel extends ValidateErrorHead {

    @ExcelProperty(index = 0, value = {"心耀商品>进销差率", "进销差率(%)"})
    @NotBlank(message = "进销差率不能为空")
    @Pattern(regexp = PERCENT, message = "进销差率请填写数字")
    private String valueStr;

    @ExcelIgnore
    private BigDecimal value;

    @ExcelIgnore
    private String unit;

    public void initBigDecimal() {
        this.value = Str2Decimal.convertValue(this.valueStr);
    }

}
