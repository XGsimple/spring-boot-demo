package com.xkcoding;

import com.xkcoding.component.R;
import com.xkcoding.component.User;
import com.xkcoding.controller.UserController;
import com.xkcoding.service.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xugangq
 * @description
 * @createTime 2021/3/23 13:08
 */
@ExtendWith(SpringExtension.class)
public class UserControllerMockitoTest {
    @MockBean
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    /**
     * 新增用户测试用例
     *
     * @throws Exception
     */
    @Test
    @DisplayName("新增用户测试用例")
    public void saveUser() throws Exception {
        R res = userController.saveUser(new User("小明"));
        assertThat(res).isEqualTo(R.ok());

    }

}
