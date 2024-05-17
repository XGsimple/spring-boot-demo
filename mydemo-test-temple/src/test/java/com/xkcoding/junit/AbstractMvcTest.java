package com.xkcoding.junit;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@MockitoSettings(strictness = Strictness.WARN)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractMvcTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void contextLoads() {
    }

    public MvcResult testRequest(String url, HttpMethod httpMethod, Object content) throws Exception {
        if (HttpMethod.GET.equals(httpMethod)) {
            return testGet(url);
        } else if (HttpMethod.POST.equals(httpMethod)) {
            if (content instanceof String) {
                return testPost(url, (String)content);
            } else {
                return testPost(url, JSON.toJSONString(content));
            }
        } else {
            throw new RuntimeException("Unsupported http method");
        }
    }

    public MvcResult testGet(String url) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url)
                                                                             .accept(MediaType.APPLICATION_JSON)
                                                                             .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mvc.perform(requestBuilder)
                                 .andExpect(MockMvcResultMatchers.status().isOk())
                                 .andDo(MockMvcResultHandlers.print())
                                 .andReturn();
        return mvcResult;
    }

    public MvcResult testPost(String url, String contentStr) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(url)
                                                                             .accept(MediaType.APPLICATION_JSON)
                                                                             .contentType(MediaType.APPLICATION_JSON)
                                                                             .content(contentStr.getBytes());
        MvcResult mvcResult = mvc.perform(requestBuilder)
                                 .andExpect(MockMvcResultMatchers.status().isOk())
                                 .andDo(MockMvcResultHandlers.print())
                                 .andReturn();
        return mvcResult;
    }

    public MvcResult testPost(String url, Object content) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(url)
                                                                             .accept(MediaType.APPLICATION_JSON)
                                                                             .contentType(MediaType.APPLICATION_JSON)
                                                                             .content(JSON.toJSONString(content)
                                                                                          .getBytes());
        MvcResult mvcResult = mvc.perform(requestBuilder)
                                 .andExpect(MockMvcResultMatchers.status().isOk())
                                 .andDo(MockMvcResultHandlers.print())
                                 .andReturn();
        return mvcResult;
    }
}
