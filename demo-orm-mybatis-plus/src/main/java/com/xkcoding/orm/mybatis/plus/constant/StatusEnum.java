package com.xkcoding.orm.mybatis.plus.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用枚举
 * https://baomidou.com/pages/8390a4/#%E6%AD%A5%E9%AA%A41-%E5%A3%B0%E6%98%8E%E9%80%9A%E7%94%A8%E6%9E%9A%E4%B8%BE%E5%B1%9E%E6%80%A7
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {
    LOGIC_DELETE(-1, "逻辑删除"),

    DISABLE(0, "禁用"),

    ENABLE(1, "启用");

    /**
     * 将注解标识的属性值存储到数据库中
     */
    @EnumValue
    private Integer code;
    /**
     * 将注解标识的属性值返回给前端
     */
    private String name;

    /**
     * 通用枚举——重写toString
     *
     * @return
     */
    @Override
    public String toString() {
        return name;
    }
}
