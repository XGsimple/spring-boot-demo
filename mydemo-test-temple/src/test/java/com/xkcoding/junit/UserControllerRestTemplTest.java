package com.xkcoding.junit;

import com.xkcoding.junit.component.User;
import com.xkcoding.junit.dao.OrmUserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.Duration;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xugangq
 * @description
 * @createTime 2021/3/23 13:08
 */
@MockitoSettings(strictness = Strictness.WARN)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)//包含了 @ExtendWith(SpringExtension.class)
public class UserControllerRestTemplTest {
    @MockBean
    private OrmUserDao userDao;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String url;

    @BeforeEach
    public void setup() {
        url = String.format("http://localhost:%d", port);
    }

    @TestConfiguration
    static class TestRestTemplateAuthenticationConfiguration {

        @Value("${spring.security.user.name:小明}")
        private String userName;

        @Value("${spring.security.user.password:123456}")
        private String password;

        @Bean
        public RestTemplateBuilder restTemplateBuilder() {
            //            return new RestTemplateBuilder().rootUri("http://localhost:8080/").setConnectTimeout(Duration.ofMillis(1000))
            //                                            .setReadTimeout(Duration.ofMillis(1000)).basicAuthentication(userName, password);
            return new RestTemplateBuilder().setConnectTimeout(Duration.ofMillis(1000))
                                            .setReadTimeout(Duration.ofMillis(1000));
        }
    }

    /**
     * 新增用户测试用例
     *
     * @throws Exception
     */
    @Test
    @DisplayName("新增用户测试用例")
    public void addLearn() throws Exception {
        MultiValueMap<String, String> header = new LinkedMultiValueMap();
        header.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));
        header.put(HttpHeaders.ACCEPT, Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));

        HttpEntity<User> request = new HttpEntity<>(new User("小明"), header);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url + "/user/save", request, String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
