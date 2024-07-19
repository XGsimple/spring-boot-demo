package com.xkcoding.excel.multiwrite.enums;

import com.xkcoding.common.enums.InnerEnum;

/**
 * 单位
 *
 * @author Lenovo
 * @date 2024/03/07 15:55
 **/
public enum Unit implements InnerEnum.StrCodeEnum {
    PER_CENT("PER_CENT", "%"),
    PIECE("PIECE", "个"),
    YUAN("YUAN", "元"),
    WAN_YUAN("WAN_YUAN", "万元"),
    WAN("WAN", "万"),
    DAY("DAY", "天"),
    HOME("HOME", "家"),
    ;

    private final InnerEnum<String, String> innerEnum;

    Unit(String code, String msg) {
        innerEnum = new InnerEnum<>(code, msg);
    }

    @Override
    public InnerEnum<String, String> get() {
        return innerEnum;
    }
}
