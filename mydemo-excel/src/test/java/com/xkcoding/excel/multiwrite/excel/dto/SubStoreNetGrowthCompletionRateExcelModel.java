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
 * 门店发展>新开净增长门店及开店达成率-子公司
 *
 * @Author WuHuaQiang
 * @Date 2024/03/14 20:05
 **/

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubStoreNetGrowthCompletionRateExcelModel extends ValidateErrorHead {

    @ExcelProperty(index = 0, value = {"门店发展>新开净增长门店及开店达成率-子公司", "公司名称"})
    @NotBlank(message = "公司名称不能为空")
    private String key;

    @ExcelProperty(index = 1, value = {"门店发展>新开净增长门店及开店达成率-子公司", "净增长门店(家)"})
    @NotBlank(message = "净增长门店不能为空")
    @Pattern(regexp = PERCENT, message = "开店达成率请填写数字")
    private String netGrowthStoreStr;

    @ExcelProperty(index = 2, value = {"门店发展>新开净增长门店及开店达成率-子公司", "开店达成率(%)"})
    @NotBlank(message = "开店达成率不能为空")
    @Pattern(regexp = PERCENT, message = "开店达成率请填写数字")
    private String beginStoreCompletionRateStr;

    @ExcelIgnore
    private String beginStoreCompletionRateUnit;

    @ExcelIgnore
    private BigDecimal netGrowthStore;

    @ExcelIgnore
    private BigDecimal beginStoreCompletionRate;

    public void initBigDecimal() {
        this.netGrowthStore = Str2Decimal.convertValue(this.netGrowthStoreStr);
        this.beginStoreCompletionRate = Str2Decimal.convertValue(this.beginStoreCompletionRateStr);
    }
}
