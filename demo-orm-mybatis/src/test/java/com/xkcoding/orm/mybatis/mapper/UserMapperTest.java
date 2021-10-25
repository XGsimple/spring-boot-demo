package com.xkcoding.orm.mybatis.mapper;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.xkcoding.orm.mybatis.SpringBootDemoOrmMybatisApplicationTests;
import com.xkcoding.orm.mybatis.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>
 * UserMapper 测试类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-11-08 11:25
 */
@Slf4j
public class UserMapperTest extends SpringBootDemoOrmMybatisApplicationTests {
    @Autowired
    private UserMapper userMapper;

    /**
     * 测试查询所有
     */
    @Test
    public void selectAllUser() {
        List<User> userList = userMapper.selectAllUser();
        assertThat(userList).isNotEmpty();
        log.debug("【userList】= {}", userList);
    }

    /**
     * 测试根据主键查询单个
     */
    @Test
    public void selectUserById() {
        User user = userMapper.selectUserById(1L);
        assertThat(user).isNotNull();
        log.debug("【user】= {}", user);
    }

    /**
     * 测试保存
     */
    @Test
    public void saveUser() {
        String salt = IdUtil.fastSimpleUUID();
        User user = User.builder()
                        .name("testSave3")
                        .password(SecureUtil.md5("123456" + salt))
                        .salt(salt)
                        .email("testSave3@xkcoding.com")
                        .phoneNumber("17300000003")
                        .status(1)
                        .lastLoginTime(new DateTime())
                        .createTime(new DateTime())
                        .lastUpdateTime(new DateTime())
                        .build();
        int i = userMapper.saveUser(user);
        assertThat(i).isEqualTo(1);
    }

    /**
     * 测试根据主键删除
     */
    @Test
    public void deleteById() {
        int i = userMapper.deleteById(1L);
        assertThat(i).isEqualTo(1);
    }
}
