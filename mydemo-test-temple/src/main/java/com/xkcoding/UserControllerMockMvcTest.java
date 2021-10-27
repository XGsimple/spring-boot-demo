package com.xkcoding;

import com.alibaba.fastjson.JSON;
import com.xkcoding.component.User;
import com.xkcoding.dao.UserDao;
import com.xkcoding.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

/**
 * @author xugangq
 * @description
 * @createTime 2021/3/23 13:08
 */
@MockitoSettings(strictness = Strictness.WARN)
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerMockMvcTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MockHttpSession session;

    //MockBean注解将Mock对象添加到Spring上下文中,替换Spring上下文中任何相同类型的现有bean，如果没有定义相同类型的bean，将添加一个新的bean
    @MockBean
    private UserDao userDao;

    //将Mock对象userDao注入
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setupMockMvc() {
        User user = new User(1, "小明");
        session.setAttribute("user", user); //拦截器那边会判断用户是否登录，所以这里注入一个用户
    }

    /**
     * 新增用户测试用例
     *
     * @throws Exception
     */
    @Test
    @DisplayName("新增用户测试用例")
    public void saveUser() throws Exception {
        User user = new User("小明");
        String json = JSON.toJSONString(user);
        mvc.perform(MockMvcRequestBuilders.post("/user/save").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                                          .content(json.getBytes()) //传json参数
                                          .session(session)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    /**
     * 获取用户测试用例
     * ResultActions.andDo添加一个结果处理器，表示要对结果做点什么事情，比如此处使用MockMvcResultHandlers.print()输出整个响应结果信息
     *
     * @throws Exception
     */
    @Test
    @DisplayName("获取用户测试用例")
    public void queryUser() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/query/1001").contentType(MediaType.APPLICATION_JSON)
                                                                             .accept(MediaType.APPLICATION_JSON).session(this.session);
        MvcResult mvcResult = mvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk())
                                 .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("小明")).andDo(MockMvcResultHandlers.print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        System.out.println(contentAsString);
    }

    /**
     * 修改用户测试用例
     *
     * @throws Exception
     */
    @Test
    @DisplayName("修改用户测试用例")
    public void updateUser() throws Exception {
        User user = new User("新小明");
        String json = JSON.toJSONString(user);
        mvc.perform(MockMvcRequestBuilders.post("/user/update").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                                          .content(json.getBytes())//传json参数
                                          .session(session)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    /**
     * 删除用户测试用例
     *
     * @throws Exception
     */
    @Test
    @DisplayName("删除用户测试用例")
    public void deleteUser() throws Exception {
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(1);
        String json = JSON.toJSONString(ids);
        mvc.perform(MockMvcRequestBuilders.post("/user/delete").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                                          .content(json.getBytes())//传json参数
                                          .session(session)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }
}
