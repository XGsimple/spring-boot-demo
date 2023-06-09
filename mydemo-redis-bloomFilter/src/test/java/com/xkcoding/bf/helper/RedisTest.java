package com.xkcoding.bf.helper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Redis测试
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-11-15 17:17
 */
@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 测试 Redis 操作
     */
    @Test
    public void get() {
        stringRedisTemplate.opsForValue().set("k1", "v1", 30, TimeUnit.SECONDS);
        String k1 = stringRedisTemplate.opsForValue().get("k1");
        System.out.printf(k1);
    }
}
