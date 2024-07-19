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
 * 加盟店>各阶段门店统计-子公司
 *
 * @Author WuHuaQiang
 * @Date 2024/03/14 20:19
 **/
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubStageStoreCountExcelModel extends ValidateErrorHead {

    @ExcelProperty(index = 0, value = {"加盟店>各阶段门店统计-子公司", "公司名称"})
    @NotBlank(message = "公司名称不能为空")
    private String key;

    @ExcelProperty(index = 1, value = {"加盟店>各阶段门店统计-子公司", "签约门店(家)"})
    @NotBlank(message = "签约门店不能为空")
    @Pattern(regexp = PERCENT, message = "签约门店请填写数字")
    private String agencyStoreStr;

    @ExcelProperty(index = 2, value = {"加盟店>各阶段门店统计-子公司", "筹建门店(家)"})
    @NotBlank(message = "筹建门店不能为空")
    @Pattern(regexp = PERCENT, message = "筹建门店数请填写数字")
    private String buildStoreStr;

    @ExcelProperty(index = 3, value = {"加盟店>各阶段门店统计-子公司", "开业门店(家)"})
    @NotBlank(message = "开业门店不能为空")
    @Pattern(regexp = PERCENT, message = "开业门店请填写数字")
    private String beginStoreStr;

    @ExcelIgnore
    private BigDecimal beginStore;
    @ExcelIgnore
    private BigDecimal buildStore;
    @ExcelIgnore
    private BigDecimal agencyStore;

    public void initBigDecimal() {
        this.beginStore = Str2Decimal.convertValue(this.beginStoreStr);
        this.buildStore = Str2Decimal.convertValue(this.buildStoreStr);
        this.agencyStore = Str2Decimal.convertValue(this.agencyStoreStr);
    }
}
