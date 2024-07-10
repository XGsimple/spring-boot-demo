package com.xkcoding.cache.redis;

import com.xkcoding.cache.redis.entity.User;
import com.xkcoding.cache.redis.lock.RedisUtil;
import com.xkcoding.cache.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * <p>
 * Redis测试
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-11-15 17:17
 */
@Slf4j
public class RedisTest extends SpringBootDemoCacheRedisApplicationTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisCacheTemplate;

    @Autowired
    private RedisService redisService;

    /**
     * 测试 Redis 操作
     */
    @Test
    public void testStringRedisTemplateGet() {
        // 测试线程安全，程序结束查看redis中count的值是否为1000
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        stringRedisTemplate.opsForValue().set("count", "0", 30, TimeUnit.SECONDS);
        IntStream.range(0, 1000)
                 .forEach(i -> executorService.execute(() -> stringRedisTemplate.opsForValue().increment("count", 1)));

        stringRedisTemplate.opsForValue().set("k1", "v1", 30, TimeUnit.SECONDS);
        String k1 = stringRedisTemplate.opsForValue().get("k1");
        log.debug("【k1】= {}", k1);

        // 以下演示整合，具体Redis命令可以参考官方文档
        String key = "xkcoding:user:1";
        redisCacheTemplate.opsForValue().set(key, new User(1L, "user1"), 30, TimeUnit.SECONDS);
        // 对应 String（字符串）
        User user = (User)redisCacheTemplate.opsForValue().get(key);
        log.debug("【user】= {}", user);
        executorService.shutdown();
    }

    @Test
    public void testRedisServiceGet() {
        // 测试线程安全，程序结束查看redis中count的值是否为1000
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        redisService.set("count", 0, 30);
        IntStream.range(0, 1000).forEach(i -> executorService.execute(() -> redisService.incr("count", 1L)));

        redisService.set("k1", "v1", 30);
        String k1 = (String)redisService.get("k1");
        log.debug("【k1】= {}", k1);
        executorService.shutdown();
    }

    @Test
    public void testRedisUtilGet() {
        // 测试线程安全，程序结束查看redis中count的值是否为1000
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        RedisUtil.set("count", 0, 30, TimeUnit.SECONDS);
        IntStream.range(0, 1000).forEach(i -> executorService.execute(() -> RedisUtil.incrBy("count", 1L)));

        RedisUtil.set("k1", "v1", 30, TimeUnit.SECONDS);
        String k1 = RedisUtil.get("k1");
        log.debug("【k1】= {}", k1);
        String key = "xkcoding:user:1";
        RedisUtil.set(key, new User(1L, "user1"), 30, TimeUnit.SECONDS);
        // 对应 String（字符串）
        User user = RedisUtil.get(key, User.class);
        log.debug("【user】= {}", user);
        executorService.shutdown();
    }
}
