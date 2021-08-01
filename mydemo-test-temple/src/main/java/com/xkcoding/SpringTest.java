package com.xkcoding;

import com.xkcoding.component.Apple;
import com.xkcoding.config.ProfileConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author xugangq
 * @description
 * @createTime 2021/3/23 13:08
 */
@Slf4j
@ExtendWith(SpringExtension.class) //表示该测试用例是运用junit5进行测试spring，也可以换成其他测试框架
@ContextConfiguration(classes = {ProfileConfig.class}) //此注解用来加载配置ApplicationContext
@ActiveProfiles("prod") //声明活动的profile
public class SpringTest {
    @Autowired //注入bean
    private Apple profileBean;

    @Test //@Test标注在方法前，表示其是一个测试的方法 无需在其配置文件中额外设置属性.
    @DisplayName("profile测试")
    public void prodBeanShouldInject() {
        String expected = "from production profile";
        String actual = profileBean.getName();
        assertEquals(expected, actual);
    }

    @BeforeEach
    public void beforeMethod() {
        System.out.println("before all tests");
    }

    @AfterEach
    public void afterMethod() {
        System.out.println("after all tests.");
    }
}
