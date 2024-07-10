package com.xkcoding.cache.redis.controller;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.xkcoding.cache.redis.lock.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xugangq
 * @description
 * @createTime 2020/8/27 19:37
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class RedisTestController {
    @Autowired
    private RedisLock redisLock;
    int count = 0;

    @GetMapping("/redislock")
    public Object redislockTest() throws InterruptedException {

        int clientcount = 10;
        CountDownLatch countDownLatch = new CountDownLatch(clientcount);

        ExecutorService executorService = Executors.newFixedThreadPool(clientcount);
        long start = System.currentTimeMillis();
        String lock_key = "redis_lock"; //锁键
        Snowflake snowflake = IdUtil.getSnowflake(0, 0);
        for (int i = 0; i < clientcount; i++) {
            executorService.execute(() -> {
                //通过Snowflake算法获取唯一的ID字符串
                String id = snowflake.nextId() + "";
                try {
                    redisLock.tryLock(lock_key, id);
                    count++;
                } finally {
                    redisLock.unlock(lock_key, id);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        long end = System.currentTimeMillis();
        log.info("执行线程数:{},总耗时:{},count数为:{}", clientcount, end - start, count);
        return "hello";
    }
}
