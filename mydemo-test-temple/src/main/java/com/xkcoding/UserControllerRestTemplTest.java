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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xugangq
 * @description
 * @createTime 2021/3/23 13:08
 */
@ExtendWith(SpringExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerRestTemplTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private MockHttpSession session;

    private String url;

    @BeforeEach
    public void setup() {
        url = String.format("http://localhost:%d", port);
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
        /**
         * postForObject 返回值为响应的数据
         * 参数1 要请求地址的url
         * 参数2 通过LinkedMultiValueMap对象封装请求参数  模拟表单参数，封装在请求体中
         * 参数3 响应数据的类型
         */
        LinkedMultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.set("user", json);

        Integer result = restTemplate.postForObject(url + "/user/save", request, Integer.class);
        assertThat(result).isEqualTo(1);
    }

}
