package com.xkcoding.junit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Spring Boot Demo Orm 系列示例表(OrmUser)表实体类
 *
 * @author makejava
 * @since 2024-05-17 17:11:47
 */
@Data
@TableName("orm_role")
@Accessors(chain = true)
public class OrmUser {
    //主键
    private Integer id;
    //用户名
    private String name;
    //加密后的密码
    private String password;
    //加密使用的盐
    private String salt;
    //邮箱
    private String email;
    //手机号码
    private String phoneNumber;
    //状态，-1：逻辑删除，0：禁用，1：启用
    private Integer status;
    //创建时间
    private Date createTime;
    //上次登录时间
    private Date lastLoginTime;
    //上次更新时间
    private Date lastUpdateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

}

