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
 * 加盟店>累计开业门店数
 *
 * @Author WuHuaQiang
 * @Date 2024/03/14 20:17
 **/
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StartCumulativeCountExcelModel extends ValidateErrorHead {

    @ExcelProperty(index = 0, value = {"加盟店>累计开业门店数", "累计开业门店数(家)"})
    @NotBlank(message = "累计开业门店数不能为空")
    @Pattern(regexp = PERCENT, message = "累计开业门店数请填写数字")
    private String valueStr;

    @ExcelIgnore
    private BigDecimal value;

    public void initBigDecimal() {
        this.value = Str2Decimal.convertValue(this.valueStr);
    }
}
