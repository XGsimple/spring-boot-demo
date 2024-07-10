package com.xkcoding.cache.redis.config;

import com.xkcoding.cache.redis.lock.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author XuGang
 * @Date 2024/2/19 16:13
 */
@Component
public class ProjectStartCommandLineRunner implements CommandLineRunner {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void run(String... args) throws Exception {
        RedisUtil.setRedisTemplate(stringRedisTemplate);
    }
}