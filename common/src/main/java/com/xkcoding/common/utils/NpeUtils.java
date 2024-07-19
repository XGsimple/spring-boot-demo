package com.xkcoding.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 防止出现NPE异常
 *
 * @Author XuGang
 * @Date 2024/4/2 14:28
 */
public class NpeUtils {
    /**
     * 获取对象的属性（带判断null)
     *
     * @param source        原对象（需要比较的对象）
     * @param supplier      工厂方法
     * @param defaultObject 默认值
     * @param <E>           输入的对象
     * @param <R>           输出的值
     * @return
     */
    public static <E, R> R getAttr(E source, Supplier<R> supplier, R defaultObject) {

        if (Optional.ofNullable(source).isPresent()) {

            return supplier.get();
        } else {

            return defaultObject;
        }
    }

    /**
     * 属性互转模板方法
     *
     * @param source        原对象（需要比较的对象）
     * @param function      对象E->R的赋值过程
     * @param defaultObject 默认值
     * @param <E>           原始对象
     * @param <R>           结果对象
     * @return
     */
    public static <E, R> R transfer(E source, Function<E, R> function, R defaultObject) {
        return Optional.ofNullable(source).map(function).orElse(defaultObject);
    }

    /**
     * 转换null值的模板方法
     *
     * @param value        输入的值
     * @param defaultValue 默认值
     * @param <R>          输入的对象的类型
     * @return
     */
    public static <R> R wrapNull(R value, R defaultValue) {
        return Optional.ofNullable(value).orElse(defaultValue);
    }

    /**
     * 转换BigDecimal（浮点数）数值可能为null的情况
     *
     * @param value 输入数值
     * @return 默认返回2位数，四舍五入
     * 如果为null，则返回0
     */
    public static BigDecimal wrapNull(BigDecimal value) {
        return wrapNull(value, 2, BigDecimal.ZERO);
    }

    /**
     * 转换BigDecimal（浮点数）数值可能为null的情况，如果为null，则返回0
     *
     * @param value 输入数值
     * @param scale 指定小数位
     * @return 默认四舍五入
     * 如果为null，则返回0
     */
    public static BigDecimal wrapNull(BigDecimal value, int scale) {
        return wrapNull(value, scale, BigDecimal.ZERO);
    }

    /**
     * 转换BigDecimal（浮点数）数值可能为null的情况
     *
     * @param value        输入数值
     * @param scale        指定小数位
     * @param defaultValue 指定默认值
     * @return 默认四舍五入
     * 如果为null，则返回 defaultValue
     */
    public static BigDecimal wrapNull(BigDecimal value, int scale, BigDecimal defaultValue) {
        return Optional.ofNullable(value).map(e -> e.setScale(scale, RoundingMode.HALF_UP)).orElse(defaultValue);
    }
}