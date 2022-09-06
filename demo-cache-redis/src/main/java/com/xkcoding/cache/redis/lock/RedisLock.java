package com.xkcoding.cache.redis.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author xugangq
 * @description
 * @createTime 2020/8/27 19:08
 */
@Slf4j
@Component
public class RedisLock implements IRedisLockService {
    //默认锁时长
    public static final int DEFAULT_SECOND_LEN = 10;
    private static final String LOCK_LUA = "if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then redis.call('expire', KEYS[1], ARGV[2]) return 'true' else return 'false' end";
    private static final String UNLOCK_LUA = "if redis.call('get', KEYS[1]) == ARGV[1] then redis.call('del', KEYS[1]) end return 'true' ";

    private RedisTemplate<String, String> redisTemplate;

    private RedisScript lockRedisScript;
    private RedisScript unLockRedisScript;

    private RedisSerializer<String> argsSerializer;
    private RedisSerializer<String> resultSerializer;

    /**
     * 初始化lua 脚本
     */
    public void init(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;

        argsSerializer = new StringRedisSerializer();
        resultSerializer = new StringRedisSerializer();

        lockRedisScript = RedisScript.of(LOCK_LUA, String.class);
        unLockRedisScript = RedisScript.of(UNLOCK_LUA, String.class);
    }

    @Override
    public boolean tryLock(String lock, String val) {
        return this.tryLock(lock, val, DEFAULT_SECOND_LEN);
    }

    @Override
    public boolean tryLock(String lock, String val, int second) {
        List<String> keys = Collections.singletonList(generateRedisKey(lock));
        boolean locked = false;
        int tryCount = 5;
        //重试
        while (!locked && tryCount > 0) {
            String flag = redisTemplate.execute(lockRedisScript, argsSerializer, resultSerializer, keys, val, String.valueOf(second));
            locked = Boolean.valueOf(flag);
            tryCount--;
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                log.error("线程被中断" + Thread.currentThread().getId(), e);
            }
        }
        return locked;
    }

    @Override
    public void unlock(String lock, String val) {
        List<String> keys = Collections.singletonList(generateRedisKey(lock));
        redisTemplate.execute(unLockRedisScript, argsSerializer, resultSerializer, keys, val);
    }

    /**
     * 生产redis的key
     */
    public String generateRedisKey(String lock) {
        log.debug("#####redisLockKey:{}#####", lock);
        return lock;
    }
}
