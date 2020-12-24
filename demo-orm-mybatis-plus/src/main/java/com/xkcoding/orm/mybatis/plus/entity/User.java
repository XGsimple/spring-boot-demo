package com.xkcoding.orm.mybatis.plus.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户实体类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-11-08 16:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("orm_user")
public class User implements Serializable {
    private static final long serialVersionUID = -1840831686851699943L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    //@TableField(value = "name")//字段名与数据库字段名不一致时采用该形式进行映射
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 加密后的密码
     */
    private String password;

    /**
     * 加密使用的盐
     */
    private String salt;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 状态，-1：逻辑删除，0：禁用，1：启用
     */
    private Integer status;

    /**
     * 版本号，用于乐观锁
     */
    @Version//乐观锁注解
    @TableField(fill = FieldFill.INSERT)//插入数据时填充
    private Integer version;

    /**
     * 逻辑删除
     */
    @TableField(fill = FieldFill.INSERT)//插入数据时填充
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 上次登录时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)//插入和更新操作时自动更新时间
    private Date lastLoginTime;

    /**
     * 上次更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date lastUpdateTime;
}
