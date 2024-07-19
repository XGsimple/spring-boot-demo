package com.xkcoding.excel.multiwrite.enums;

import com.xkcoding.common.enums.InnerEnum;

/**
 * 导入文件来源枚举
 *
 * @Author XuGang
 * @Date 2024/03/08 16:44
 */
public enum ImportFileSourceEnum implements InnerEnum.StrCodeEnum {

    OBS("OBS", "OBS云端存储，输入流"),
    LOCAL("LOCAL", "本地文件"),
    ;

    private final InnerEnum<String, String> innerEnum;

    ImportFileSourceEnum(String code, String msg) {
        innerEnum = new InnerEnum<>(code, msg);
    }

    @Override
    public InnerEnum<String, String> get() {
        return innerEnum;
    }
}
