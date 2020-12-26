package com.xkcoding.cache.redis.service.impl;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.xkcoding.cache.redis.entity.User;
import com.xkcoding.cache.redis.service.RedisService;
import com.xkcoding.cache.redis.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * UserService
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-11-15 16:45
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    /**
     * 模拟数据库
     */
    private static final Map<Long, User> DATABASES = Maps.newConcurrentMap();

    /**
     * 初始化数据
     */
    static {
        DATABASES.put(1L, new User(1L, "user1"));
        DATABASES.put(2L, new User(2L, "user2"));
        DATABASES.put(3L, new User(3L, "user3"));
    }

    @Autowired
    RedisService redisService;

    //==============================基于@Cache注解，自动管理缓存操作==============================
    /**
     * 保存或修改用户
     *
     * @param user 用户对象
     * @return 操作结果
     */
    @CachePut(value = "user", key = "#user.id")
    @Override
    public User saveOrUpdate(User user) {
        DATABASES.put(user.getId(), user);
        log.info("保存用户【user】= {}", user);
        return user;
    }

    /**
     * 获取用户
     *
     * @param id key值
     * @return 返回结果
     */
    @Cacheable(value = "user", key = "#id")
    @Override
    public User get(Long id) {
        // 我们假设从数据库读取
        log.info("查询用户【id】= {}", id);
        return DATABASES.get(id);
    }

    /**
     * 删除
     *
     * @param id key值
     */
    @CacheEvict(value = "user", key = "#id")
    @Override
    public void delete(Long id) {
        DATABASES.remove(id);
        log.info("删除用户【id】= {}", id);
    }

    //==============================基于redisTemplate，手动管理缓存操作==============================
    /**
     * 基于redisTemple
     *
     * @param user 用户对象
     */
    @Override
    public User saveOrUpdateByRedisTemple(User user) {
        DATABASES.put(user.getId(), user);
        log.info("保存用户【user】= {}", user);
        String redisKey = StrUtil.format("user::{}", user.getId());
        redisService.set(redisKey, user);
        return user;
    }

    @Override
    public User getByRedisTemple(Long id) {
        String redisKey = StrUtil.format("user::{}", id);
        User user = (User) redisService.get(redisKey);
        if (Objects.isNull(user)) {
            // 我们假设从数据库读取
            log.info("查询用户【id】= {}", id);
            user = DATABASES.get(id);
            //保存到redis中
            redisService.set(redisKey, user);
        }
        return user;
    }

    @Override
    public void deleteByRedisTemple(Long id) {
        String redisKey = StrUtil.format("user::{}", id);
        redisService.del(redisKey);
        DATABASES.remove(id);
        log.info("删除用户【id】= {}", id);
    }
}
