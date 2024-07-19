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
 * 加盟店>累计开业门店-子公司
 *
 * @Author WuHuaQiang
 * @Date 2024/03/14 20:22
 **/

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubStartCumulativeCountExcelModel extends ValidateErrorHead {

    @ExcelProperty(index = 0, value = {"加盟店>累计开业门店-子公司", "公司名称"})
    @NotBlank(message = "公司名称不能为空")
    private String key;

    @ExcelProperty(index = 1, value = {"加盟店>累计开业门店-子公司", "累计开业门店(家)"})
    @NotBlank(message = "累计开业门店不能为空")
    @Pattern(regexp = PERCENT, message = "累计开业门店请填写数字")
    private String valueStr;

    @ExcelIgnore
    private BigDecimal value;

    public void initBigDecimal() {
        value = Str2Decimal.convertValue(valueStr);
    }

}
