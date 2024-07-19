package com.xkcoding.excel.multiwrite.excel.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.yxt.bigdata.decision.server.monthly.common.Str2Decimal;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

import static com.yxt.bigdata.decision.server.monthly.common.Regexp.PERCENT;

/**
 * 加盟店>签约门店数
 *
 * @Author WuHuaQiang
 * @Date 2024/03/14 20:14
 **/

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreAgencyCountExcelModel extends ValidateErrorHead {

    @ExcelProperty(index = 0, value = {"加盟店>签约门店数", "签约门店数(家)"})
    @NotBlank(message = "签约门店数不能为空")
    @Pattern(regexp = PERCENT, message = "签约门店数请填写数字")
    private String valueStr;

    private BigDecimal value;

    public void initBigDecimal() {
        this.value = Str2Decimal.convertValue(this.valueStr);
    }
}
