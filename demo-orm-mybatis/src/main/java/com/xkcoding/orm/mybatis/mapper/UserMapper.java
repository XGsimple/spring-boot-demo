package com.xkcoding.orm.mybatis.mapper;

import com.xkcoding.orm.mybatis.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * User Mapper
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-11-08 10:54
 */
@Mapper
@Component
public interface UserMapper {

    /**
     * 查询所有用户
     *
     * @return 用户列表
     */
    @Select("SELECT * FROM orm_user")
    List<User> selectAllUser();

    /**
     * 根据id查询用户
     *
     * @param id 主键id
     * @return 当前id的用户，不存在则是 {@code null}
     */
    @Select("SELECT * FROM orm_user WHERE id = #{id}")
    User selectUserById(@Param("id") Long id);

    /**
     * 保存用户
     *
     * @param user 用户
     * @return 成功 - {@code 1} 失败 - {@code 0}
     */
    int saveUser(@Param("user") User user);

    /**
     * 保存用户-基于注解，并返回主键
     *
     * @param user 用户
     * @return 成功 - {@code 1} 失败 - {@code 0}
     */
    //返回非自增主键
    //@SelectKey(statement = "SELECT LAST INSERT ID {)", keyProperty = "id", resultType = Long.class, before = false)
    //返回自增主键
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO orm_user (name,password,salt,email,phone_number,status,create_time,last_login_time,last_update_time)" +
            "VALUES (#{user.name},#{user.password},#{user.salt},#{user.email},#{user.phoneNumber},#{user.status},#{user.createTime},#{user.lastLoginTime},#{user.lastUpdateTime})")
    int saveUserByAnnotation(User user);

    /**
     * 删除用户
     *
     * @param id 主键id
     * @return 成功 - {@code 1} 失败 - {@code 0}
     */
    int deleteById(@Param("id") Long id);

}
