package com.ycy.druid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//启注解事务管理
@EnableTransactionManagement
public class SpringBootDemoDruidApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoDruidApplication.class, args);
    }

}
