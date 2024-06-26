package com.xkcoding.elasticsearch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * 启动类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-10-27 22:52
 */
@MapperScan("com.xkcoding.elasticsearch.mapper")
@SpringBootApplication
public class SpringBootDemoElasticsearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoElasticsearchApplication.class, args);
    }

}
