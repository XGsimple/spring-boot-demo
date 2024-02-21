package com.xkcoding.bf.enums;

import cn.hutool.core.util.ArrayUtil;

import java.text.MessageFormat;
import java.util.function.Supplier;

/**
 * @Author XuGang
 * @Date 2023/11/1 17:31
 */
public class InnerEnum<K, V> {

    private final K code;
    private final V value;

    public InnerEnum(K code, V value) {
        this.code = code;
        this.value = value;
    }

    public K getCode() {
        return code;
    }

    public V getValue() {
        return value;
    }

    /**
     * Integer类型Code枚举
     */
    public interface IntegerCodeEnum extends Supplier<InnerEnum<Integer, String>> {
        default Integer getCode() {
            return get().getCode();
        }

        default String getMsg() {
            return get().getValue();
        }

        //会处理可能含占位符情况
        default String getMsg(Object... placeHolders) {
            String result = getMsg();
            if (ArrayUtil.isNotEmpty(placeHolders)) {
                //处理包含占位符的情况
                result = MessageFormat.format(getMsg(), placeHolders);
            }
            return result;
        }

        //        static <T extends Enum<T> & IntValueEnum> T valueOf(Class<T> enumType, int value) {
        //            for (T c : enumType.getEnumConstants()) {
        //                if (c.getValue().equals(value)) {
        //                    return c;
        //                }
        //            }
        //            return null;
        //        }
    }

    /**
     * String 类型Code枚举
     */
    public interface StrCodeEnum extends Supplier<InnerEnum<String, String>> {
        default String getCode() {
            return get().getCode();
        }

        default String getMsg() {
            return get().getValue();
        }

        //会处理可能含占位符情况
        default String getMsg(Object... placeHolders) {
            String result = getMsg();
            if (ArrayUtil.isNotEmpty(placeHolders)) {
                //处理包含占位符的情况
                result = MessageFormat.format(getMsg(), placeHolders);
            }
            return result;
        }
    }

    /**
     * Long 类型Code枚举
     */
    public interface LongCodeEnum extends Supplier<InnerEnum<Long, String>> {
        default Long getCode() {
            return get().getCode();
        }

        default String getMsg() {
            return get().getValue();
        }

    }
}
