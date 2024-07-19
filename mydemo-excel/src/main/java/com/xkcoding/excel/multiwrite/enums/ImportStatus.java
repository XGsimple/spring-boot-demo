package com.xkcoding.excel.multiwrite.enums;

import com.xkcoding.common.enums.InnerEnum;
import lombok.Getter;

/**
 * @Author: ZhangCheng
 * @Date: 2024/3/7
 */
@Getter
public enum ImportStatus implements InnerEnum.StrCodeEnum {

    ING("ING", "导入中"),

    FAIL("FAIL", "解析失败"),

    SUCCESS("SUCCESS", "解析成功"),

    OVERRIDE("OVERRIDE", "已覆盖"),

    ;

    private final InnerEnum<String, String> innerEnum;

    ImportStatus(String code, String msg) {
        innerEnum = new InnerEnum<>(code, msg);
    }

    @Override
    public InnerEnum<String, String> get() {
        return innerEnum;
    }
}
