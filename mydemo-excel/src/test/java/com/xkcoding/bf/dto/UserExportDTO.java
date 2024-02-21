package com.xkcoding.bf.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 建议引进商品导出对象
 *
 * @Author XuGang
 * @Date 2024/1/23 14:19
 */
@ApiModel("用户导出")
@Getter
@Setter
@Accessors(chain = true)
@ToString(callSuper = true)
public class UserExportDTO {

    @ExcelProperty("身份证")
    private String idCard;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("手机号")
    private String phone;

}