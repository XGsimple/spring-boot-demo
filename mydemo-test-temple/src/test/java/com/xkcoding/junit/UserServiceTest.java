package com.xkcoding.junit;

import com.xkcoding.junit.dao.OrmUserDao;
import com.xkcoding.junit.entity.OrmUser;
import com.xkcoding.junit.service.impl.OrmUserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * @author xugangq
 * @date 2021/6/8 20:20
 */

@MockitoSettings(strictness = Strictness.WARN)
@SpringBootTest
public class UserServiceTest {

    //MockBean注解将Mock对象添加到Spring上下文中,替换Spring上下文中任何相同类型的现有bean，如果没有定义相同类型的bean，将添加一个新的bean
    @MockBean
    private OrmUserDao userDao;

    //将Mock对象userDao注入
    @InjectMocks
    private OrmUserServiceImpl userService;

    @Test
    @DisplayName("根据主键查询用户")
    public void selectById() throws Exception {
        // 定义当调用mock userDao的selectById()方法，并且参数为3时，就返回id为200、name为I'm mock3的user对象
        OrmUser ormUser = new OrmUser();
        ormUser.setId(300);
        ormUser.setName("I'm mock 3");
        when(userDao.selectById(3)).thenReturn(ormUser);

        // 返回的会是名字为I'm mock 3的user对象
        OrmUser user = userService.getById(3);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(300);
        assertThat(user.getName()).isEqualTo("I'm mock 3");
    }

    //以 行为驱动开发 的格式使用 //given //when //then 注释为测试用法基石编写测试用例
    @Test
    @DisplayName("根据主键查询用户-行为驱动开发")
    public void selectById2() throws Exception {
        // given
        OrmUser ormUser = new OrmUser();
        ormUser.setId(100);
        ormUser.setName("I'm mock 3");
        given(userDao.selectById(1)).willReturn(ormUser);
        // when
        OrmUser user = userService.getById(1);
        // Then
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(100);
        assertThat(user.getName()).isEqualTo("I'm mock 3");
    }

    @Test
    @DisplayName("保存用户")
    public void saveUser() {
        when(userDao.insert(Mockito.any(OrmUser.class))).thenReturn(1);
        //Boolean b = userService.save(new OrmUser()); //会返回100
        assertThat(userService.save(new OrmUser())).isEqualTo(true);
    }

    @Test
    @DisplayName("保存用户-用户为null")
    public void testNullUser() {
        assertThatThrownBy(() -> userService.saveException(null)).isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("保存用户-主键为null")
    public void testNullUserId() throws Exception {
        OrmUser user = new OrmUser();
        user.setId(null);
        assertThatThrownBy(() -> userService.saveException(user)).isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("保存用户-名称为null")
    public void testNullUserName() throws Exception {
        OrmUser user = new OrmUser();
        user.setId(1);
        user.setName("");
        assertThatThrownBy(() -> userService.saveException(user)).isInstanceOf(SQLException.class);
    }

    @Test
    @DisplayName("保存用户-用户已存在")
    public void testCreateExistUser() throws Exception {
        OrmUser returnUser = new OrmUser();
        returnUser.setId(1);
        returnUser.setName("Vikey");
        when(userDao.selectById(1)).thenReturn(returnUser);

        OrmUser user = new OrmUser();
        user.setId(1);
        user.setName("Vikey");
        assertThatThrownBy(() -> userService.saveException(user)).isInstanceOf(SQLException.class);
    }

    @Test
    @DisplayName("保存用户-抛出异常")
    public void testCreateUserOnDatabaseException1() throws Exception {
        //如果方法本身没有指定异常，则只能使用RuntimeException
        doThrow(new RuntimeException()).when(userDao).insert(any(OrmUser.class));

        OrmUser user = new OrmUser();
        user.setId(1);
        user.setName("Vikey");
        assertThatThrownBy(() -> userService.save(user)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("保存用户-Answer断言抛出异常")
    public void testCreateUserOnDatabaseException2() throws Exception {

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                throw new SQLException("SQL is not valid");
            }
        }).when(userDao).insert(any(OrmUser.class));

        OrmUser user = new OrmUser();
        user.setId(1);
        user.setName("Vikey");

        assertThatThrownBy(() -> userService.save(user)).isInstanceOf(SQLException.class)
                                                        .hasMessage("SQL is not valid");
    }

    @Test
    @DisplayName("保存用户-为无返回值的函数做测试桩")
    public void testCreateUserOnDatabaseException3() throws Exception {

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Object mock = invocation.getMock();
                return null;
            }
        }).when(userDao).insert(any(OrmUser.class));

        OrmUser user = new OrmUser();
        user.setId(1);
        user.setName("Vikey");

        assertThatThrownBy(() -> userService.saveException(user)).isInstanceOf(SQLException.class)
                                                                 .hasMessage("SQL is not valid");
    }
}
