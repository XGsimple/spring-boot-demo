package com.xkcoding.config;

import com.xkcoding.component.Apple;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author xugangq
 * @description
 * @createTime 2021/3/23 13:12
 */
@Configuration
public class ProfileConfig {
    @Bean // 声明当前方法的返回值是一个bean
    @Profile("dev")
    public Apple devTestBean() {
        return new Apple("from development profile");
    }

    @Bean
    @Profile("prod")
    public Apple prodTestBean() {
        return new Apple("from production profile");
    }
}

