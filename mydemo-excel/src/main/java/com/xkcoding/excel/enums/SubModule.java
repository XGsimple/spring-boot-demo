package com.xkcoding.bf.enums;

/**
 * @Author: ZhangCheng
 * @Date: 2024/3/7
 */
public enum SubModule implements InnerEnum.StrCodeEnum {
    TEST("TEST", "测试"),
    ;

    private final InnerEnum<String, String> innerEnum;

    SubModule(String code, String msg) {
        innerEnum = new InnerEnum<>(code, msg);
    }

    @Override
    public InnerEnum<String, String> get() {
        return innerEnum;
    }

    static {
        EnumCache.registerByValue(SubModule.class, SubModule.values(), SubModule::getCode);
    }
}
