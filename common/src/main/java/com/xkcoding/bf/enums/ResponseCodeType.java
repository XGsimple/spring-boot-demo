package com.xkcoding.bf.enums;

/**
 * http请求返回code，枚举。
 *
 * @Author XuGang
 * @Date 2023/11/2 16:16
 */
public enum ResponseCodeType implements InnerEnum.StrCodeEnum {
    SUCCESS("10000", "操作成功"),
    PARA_ERROR("20001", "参数错误"),
    SYS_EXCEPTION("50000", "开小差了，请稍后再试～"),
    BIZ_EXCEPTION("50001", "业务异常"),
    NO_PERMISSION("50002", "无访问权限"),
    NOT_LOGIN("50003", "用户未登录"),
    ;

    @Override
    public InnerEnum<String, String> get() {
        return innerEnum;
    }

    private final InnerEnum<String, String> innerEnum;

    ResponseCodeType(String code, String msg) {
        innerEnum = new InnerEnum<>(code, msg);
    }
}