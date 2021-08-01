package com.xkcoding;

import com.xkcoding.component.User;
import com.xkcoding.dao.UserDao;
import com.xkcoding.service.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * @author xugangq
 * @date 2021/6/8 20:20
 */

@ExtendWith(SpringExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
@SpringBootTest
public class UserServiceTest {

    //MockBean注解将Mock对象添加到Spring上下文中,替换Spring上下文中任何相同类型的现有bean，如果没有定义相同类型的bean，将添加一个新的bean
    @MockBean
    private UserDao userDao;

    //将Mock对象userDao注入
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("根据主键查询用户")
    public void getUserById() throws Exception {
        // 定义当调用mock userDao的getUserById()方法，并且参数为3时，就返回id为200、name为I'm mock3的user对象
        when(userDao.getUserById(3)).thenReturn(new User(300, "I'm mock 3"));

        // 返回的会是名字为I'm mock 3的user对象
        User user = userService.queryUserById(3);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(300);
        assertThat(user.getName()).isEqualTo("I'm mock 3");
    }

    //以 行为驱动开发 的格式使用 //given //when //then 注释为测试用法基石编写测试用例
    @Test
    @DisplayName("根据主键查询用户-行为驱动开发")
    public void getUserById2() throws Exception {
        // given
        given(userDao.getUserById(1)).willReturn(new User(100, "I'm mock 1"));
        // when
        User user = userService.queryUserById(1);
        // Then
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(100);
        assertThat(user.getName()).isEqualTo("I'm mock 1");
    }

    @Test
    @DisplayName("保存用户")
    public void saveUser() {
        when(userService.saveUser(Mockito.any(User.class))).thenReturn(100);
        Integer i = userService.saveUser(new User()); //会返回100
        assertThat(userService.saveUser(new User())).isEqualTo(100);
    }

    @Test
    @DisplayName("保存用户-用户为null")
    public void testNullUser() {
        assertThatThrownBy(() -> userService.createNewUser(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("保存用户-主键为null")
    public void testNullUserId() throws Exception {
        User user = new User();
        user.setId(null);
        assertThatThrownBy(() -> userService.createNewUser(user)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("保存用户-名称为null")
    public void testNullUserName() throws Exception {
        User user = new User();
        user.setId(1);
        user.setName("");
        assertThatThrownBy(() -> userService.createNewUser(user)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("保存用户-用户已存在")
    public void testCreateExistUser() throws Exception {
        User returnUser = new User();
        returnUser.setId(1);
        returnUser.setName("Vikey");
        when(userDao.getUserById(1)).thenReturn(returnUser);

        User user = new User();
        user.setId(1);
        user.setName("Vikey");
        assertThatThrownBy(() -> userService.createNewUser(user)).isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("保存用户-抛出异常")
    public void testCreateUserOnDatabaseException1() throws Exception {
        //如果方法本身没有指定异常，则只能使用RuntimeException
        doThrow(new RuntimeException()).when(userDao).insertUser(any(User.class));

        User user = new User();
        user.setId(1);
        user.setName("Vikey");
        assertThatThrownBy(() -> userService.saveUser(user)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("保存用户-Answer断言抛出异常")
    public void testCreateUserOnDatabaseException2() throws Exception {

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                throw new SQLException("SQL is not valid");
            }
        }).when(userDao).insertUser(any(User.class));

        User user = new User();
        user.setId(1);
        user.setName("Vikey");

        assertThatThrownBy(() -> userService.saveUser(user)).isInstanceOf(SQLException.class).hasMessage("SQL is not valid");
    }
}
