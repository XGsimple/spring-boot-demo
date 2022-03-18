package com.xkcoding.dynamic.datasource;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class) //表示该测试用例是运用junit5进行测试spring，也可以换成其他测试框架
@SpringBootTest
public class SpringBootDemoDynamicDatasourceApplicationTests {

    @Test
    public void contextLoads() {
    }

}
