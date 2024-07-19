package com.xkcoding.excel.multiwrite.excel.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.yxt.bigdata.decision.server.monthly.common.Str2Decimal;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

import static com.yxt.bigdata.decision.server.monthly.common.Regexp.PERCENT;

/**
 * 门店发展>新开店-累计开店达成率
 *
 * @Author WuHuaQiang
 * @Date 2024/03/14 19:51
 **/

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewStoreCompletionRateExcelModel extends ValidateErrorHead {

    @ExcelProperty(index = 0, value = {"门店发展>新开店-累计开店达成率", "累计开店达成率(%)"})
    @NotBlank(message = "累计开店达成率不能为空")
    @Pattern(regexp = PERCENT, message = "累计开店达成率请填写数字")
    private String beginCompletionRateStr;

    @ExcelIgnore
    private String beginCompletionRateUnit;

    @ExcelProperty(index = 1, value = {"门店发展>新开店-累计开店达成率", "新开店(家)"})
    @NotBlank(message = "新开门店不能为空")
    @Pattern(regexp = PERCENT, message = "新开门店请填写数字")
    private String beginStoreStr;

    @ExcelProperty(index = 2, value = {"门店发展>新开店-累计开店达成率", "搬迁(家)"})
    @NotNull(message = "搬迁不能为空")
    private Integer carriageStore;

    @ExcelProperty(index = 3, value = {"门店发展>新开店-累计开店达成率", "撤店(家)"})
    @NotNull(message = "撤店不能为空")
    private Integer evacuateStore;

    @ExcelProperty(index = 4, value = {"门店发展>新开店-累计开店达成率", "净增长(家）"})
    @NotNull(message = "净增长不能为空")
    private Integer netGrowth;

    @ExcelIgnore
    private BigDecimal beginCompletionRate;
    @ExcelIgnore
    private BigDecimal beginStore;

    public void initBigDecimal() {
        this.beginCompletionRate = Str2Decimal.convertValue(this.beginCompletionRateStr);
        this.beginStore = Str2Decimal.convertValue(this.beginStoreStr);
    }

}
