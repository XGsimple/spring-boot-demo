package com.xkcoding.orm.mybatis.plus.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.xkcoding.orm.mybatis.plus.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * UserMapper
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-11-08 16:57
 */
@Component
@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * FROM user WHERE id=#{id};")
    User myGetUserById(Long id);

    /**
     * 查询所有用户信息
     *
     * @return list
     */
    List<User> mySelectAll();

    /**
     *
     * 如果自定义的方法还希望能够使用MP提供的Wrapper条件构造器，则需要如下写法
     *
     * @param userWrapper
     * @return
     */
    List<User> mySelectByMyWrapper(@Param(Constants.WRAPPER) Wrapper<User> userWrapper);

    /**
     * 自定Wrapper修改
     *
     * @param userWrapper 条件构造器
     * @param user        修改的对象参数
     * @return
     */

    int myUpdateByMyWrapper(@Param(Constants.WRAPPER) Wrapper<User> userWrapper, @Param("user") User user);


}
