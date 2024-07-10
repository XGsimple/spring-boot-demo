package com.xkcoding.delay.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisDelayServiceTest {
    @Autowired
    RedisDelayService redisDelayService;

    @Test
    void testProductionDelayMessage() {
        redisDelayService.productionDelayMessage();
    }

    @Test
    void testConsumerDelayMessage() {
        redisDelayService.consumerDelayMessage();
    }

    @Test
    void testMain() {
        redisDelayService.productionDelayMessage();
        redisDelayService.consumerDelayMessage();
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
