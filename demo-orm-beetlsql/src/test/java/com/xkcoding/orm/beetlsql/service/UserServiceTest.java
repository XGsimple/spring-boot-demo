package com.xkcoding.orm.beetlsql.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.xkcoding.orm.beetlsql.SpringBootDemoOrmBeetlsqlApplicationTests;
import com.xkcoding.orm.beetlsql.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.beetl.sql.core.engine.PageQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * <p>
 * User Service测试
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-11-14 16:30
 */
@Slf4j
@ExtendWith(SpringExtension.class)
public class UserServiceTest extends SpringBootDemoOrmBeetlsqlApplicationTests {
    @Autowired
    private UserService userService;

    @Test
    public void saveUser() {
        String salt = IdUtil.fastSimpleUUID();
        User user = User.builder().name("testSave3").password(SecureUtil.md5("123456" + salt)).salt(salt).email("testSave3@xkcoding.com")
                        .phoneNumber("17300000003").status(1).lastLoginTime(new DateTime()).createTime(new DateTime()).lastUpdateTime(new DateTime()).build();

        user = userService.saveUser(user);
        assertThat(user.getId()).isNotNull();
        log.debug("【user】= {}", user);
    }

    @Test
    public void saveUserList() {
        List<User> users = Lists.newArrayList();
        for (int i = 5; i < 15; i++) {
            String salt = IdUtil.fastSimpleUUID();
            User user = User.builder().name("testSave" + i).password(SecureUtil.md5("123456" + salt)).salt(salt).email("testSave" + i + "@xkcoding.com")
                            .phoneNumber("1730000000" + i).status(1).lastLoginTime(new DateTime()).createTime(new DateTime()).lastUpdateTime(new DateTime())
                            .build();
            users.add(user);
        }
        userService.saveUserList(users);
        assertThat(userService.getUserList().size()).isGreaterThan(2);
    }

    @Test
    public void deleteUser() {
        userService.deleteUser(1L);
        User user = userService.getUser(1L);
        assertThat(user).isNull();
    }

    @Test
    public void updateUser() {
        User user = userService.getUser(2L);
        user.setName("beetlSql 修改后的名字");
        User update = userService.updateUser(user);
        assertThat("beetlSql 修改后的名字").isEqualTo(update.getName());
        log.debug("【update】= {}", update);
    }

    @Test
    public void getUser() {
        User user = userService.getUser(1L);
        assertThat(user).isNotNull();
        log.debug("【user】= {}", user);
    }

    @Test
    public void getUserList() {
        List<User> userList = userService.getUserList();
        assertThat(userList).isNotEmpty();
        log.debug("【userList】= {}", userList);
    }

    @Test
    public void getUserByPage() {
        List<User> userList = userService.getUserList();
        PageQuery<User> userByPage = userService.getUserByPage(1, 5);
        assertThat(userByPage.getList().size()).isEqualTo(5);
        assertThat(userList.size()).isEqualTo(userByPage.getTotalRow());
        log.debug("【userByPage】= {}", JSONUtil.toJsonStr(userByPage));
    }
}
