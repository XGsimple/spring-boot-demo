package com.xkcoding;

import com.alibaba.fastjson.JSON;
import com.xkcoding.component.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
@ExtendWith(SpringExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerMockMvcTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MockHttpSession session;

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
    public void addLearn() throws Exception {
        User user = new User("小明");
        String json = JSON.toJSONString(user);
        mvc.perform(MockMvcRequestBuilders.post("/user/save").accept(MediaType.APPLICATION_JSON).content(json.getBytes()) //传json参数
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
    public void qryLearn() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/query/1001")
                                                                             .contentType(MediaType.APPLICATION_JSON)
                                                                             .accept(MediaType.APPLICATION_JSON)
                                                                             .session(this.session);
        MvcResult mvcResult = mvc.perform(requestBuilder)
                                 .andExpect(MockMvcResultMatchers.status().isOk())
                                 .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("小明"))
                                 .andDo(MockMvcResultHandlers.print())
                                 .andReturn();
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
    public void updateLearn() throws Exception {
        User user = new User("新小明");
        String json = JSON.toJSONString(user);
        mvc.perform(MockMvcRequestBuilders.post("/user/update").accept(MediaType.APPLICATION_JSON).content(json.getBytes())//传json参数
                                          .session(session)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    /**
     * 删除用户测试用例
     *
     * @throws Exception
     */
    @Test
    @DisplayName("删除用户测试用例")
    public void deleteLearn() throws Exception {
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(1);
        String json = JSON.toJSONString(ids);
        mvc.perform(MockMvcRequestBuilders.post("/user/delete").accept(MediaType.APPLICATION_JSON).content(json.getBytes())//传json参数
                                          .session(session)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }
}
