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
 * 门店发展>并购店-累计开店达成率
 *
 * @Author WuHuaQiang
 * @Date 2024/03/14 20:01
 **/
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MAStoreCompletionRateExcelModel extends ValidateErrorHead {

    @ExcelProperty(index = 0, value = {"门店发展>并购店-累计开店达成率", "累计开店达成率(%)"})
    @NotBlank(message = "累计开店达成率不能为空")
    @Pattern(regexp = PERCENT, message = "累计开店达成率请填写数字")
    private String beginStoreCompletionRateStr;

    @ExcelIgnore
    private String beginStoreCompletionRateUnit;

    @ExcelProperty(index = 1, value = {"门店发展>并购店-累计开店达成率", "并购(家)"})
    @NotNull(message = "并购不能为空")
    private Integer mergerStore;

    @ExcelProperty(index = 2, value = {"门店发展>并购店-累计开店达成率", "累计并购(家)"})
    @NotNull(message = "累计并购不能为空")
    private Integer cumulativeMerger;

    @ExcelIgnore
    private BigDecimal beginStoreCompletionRate;

    public void initBigDecimal() {
        this.beginStoreCompletionRate = Str2Decimal.convertValue(this.beginStoreCompletionRateStr);
    }

}
