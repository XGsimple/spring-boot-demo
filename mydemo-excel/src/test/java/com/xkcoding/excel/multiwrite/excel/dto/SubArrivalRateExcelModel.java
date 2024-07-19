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
 * 到货率>到货率-子公司
 *
 * @Author WuHuaQiang
 * @Date 2024/03/14 21:48
 **/
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubArrivalRateExcelModel extends ValidateErrorHead {

    @ExcelProperty(index = 0, value = {"到货率>到货率-子公司", "公司名称"})
    @NotBlank(message = "公司名称不能为空")
    private String key;

    @ExcelProperty(index = 1, value = {"到货率>到货率-子公司", "到货率(%)"})
    @NotBlank(message = "到货率不能为空")
    @Pattern(regexp = PERCENT, message = "到货率请填写数字")
    private String arrivalRateStr;

    @ExcelIgnore
    private BigDecimal arrivalRate;

    @ExcelIgnore
    private String arrivalRateUnit;

    @ExcelProperty(index = 2, value = {"到货率>到货率-子公司", "到货率达标线(%)"})
    @NotBlank(message = "到货率达标线(%)不能为空")
    @Pattern(regexp = PERCENT, message = "到货率达标线请填写数字")
    private String targetValueStr;

    @ExcelIgnore
    private BigDecimal targetValue;

    @ExcelIgnore
    private String targetUnit;

    @ExcelProperty(index = 3, value = {"到货率>到货率-子公司", "集团到货率(%)"})
    @NotBlank(message = "集团到货率不能为空")
    @Pattern(regexp = PERCENT, message = "集团到货率请填写数字")
    private String groupArrivalRateStr;

    @ExcelIgnore
    private BigDecimal groupArrivalRate;

    @ExcelIgnore
    private String groupArrivalRateUnit;

    @ExcelProperty(index = 4, value = {"到货率>到货率-子公司", "集团环比(%)"})
    @NotBlank(message = "集团环比不能为空")
    @Pattern(regexp = PERCENT, message = "集团环比请填写数字")
    private String popValueStr;

    @ExcelIgnore
    private BigDecimal popValue;

    @ExcelIgnore
    private String popUnit;

    public void initBigDecimal() {
        arrivalRate = Str2Decimal.convertValue(arrivalRateStr);
        targetValue = Str2Decimal.convertValue(targetValueStr);
        groupArrivalRate = Str2Decimal.convertValue(groupArrivalRateStr);
        popValue = Str2Decimal.convertValue(popValueStr);
    }
}
