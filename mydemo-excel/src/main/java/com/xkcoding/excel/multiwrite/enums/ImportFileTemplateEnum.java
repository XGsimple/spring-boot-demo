package com.xkcoding.excel.multiwrite.enums;

import com.xkcoding.common.enums.InnerEnum;

/**
 * 导入模板枚举
 *
 * @Author XuGang
 * @Date 2024/03/08 16:44
 */
public enum ImportFileTemplateEnum implements InnerEnum.StrCodeEnum {

    TEST("TEST", "测试"),
    MONTH_REPORT("MONTH_REPORT", "月度汇报"),

    ;

    private final InnerEnum<String, String> innerEnum;

    ImportFileTemplateEnum(String code, String msg) {
        innerEnum = new InnerEnum<>(code, msg);
    }

    @Override
    public InnerEnum<String, String> get() {
        return innerEnum;
    }
}
