package com.xkcoding.cache.redis.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.Nullable;

/**
 * 重写一下get方法,返回null. 让他不走缓存
 * 在不使用缓存时
 */
@Slf4j
public class SydRedisCache extends RedisCache {
    /**
     * Create new {@link RedisCache}.
     *
     * @param name        must not be {@literal null}.
     * @param cacheWriter must not be {@literal null}.
     * @param cacheConfig must not be {@literal null}.
     */
    protected SydRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig) {
        super(name, cacheWriter, cacheConfig);
    }

    public SydRedisCache(RedisCache redisCache) {
        super(redisCache.getName(), redisCache.getNativeCache(), redisCache.getCacheConfiguration());
    }

    @Override
    @Nullable
    public ValueWrapper get(Object key) {
        log.info("syd cache return null ");
        return null;
    }

    @Override
    @Nullable
    public <T> T get(Object key, @Nullable Class<T> type) {
        log.info("syd cache return null ");
        return null;
    }

}