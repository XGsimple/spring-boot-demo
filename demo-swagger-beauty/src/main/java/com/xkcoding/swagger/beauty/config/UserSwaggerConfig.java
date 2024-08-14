package com.xkcoding.swagger.beauty.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Slf4j
@Configuration
@EnableSwagger2
public class UserSwaggerConfig {

    @Value("${swagger.enable:true}")
    private Boolean enable;

    @Bean
    public Docket userDocket() {
        return new Swagger2Configuration.DocketBuilder().groupName("用户管理")
                                                        .title("用户管理相关api接口文档")
                                                        .description("用户管理：增删改查")
                                                        .basePackages("com.xkcoding.swagger.beauty.controller")
                                                        .newDocket(enable);
    }

}