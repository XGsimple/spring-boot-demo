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
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * UserService
 * </p>
 * 如果没有指定key，那么将会使用SimpleKeyGenerator，其用入参作为key，如果两个方法入参一样，key将冲突，自定义一个keyGenerator来解决这个问题
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
     * CachePut 会把方法的返回值put到缓存里面缓存起来，供其它地方使用。它通常用在新增方法上
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

    @Caching(put = {@CachePut(value = "user", key = "#user.id")}, evict = {@CacheEvict(value = "user", key = "#user.id", beforeInvocation = true)})
    @Override
    public User doubleWtrite(User user) {
        DATABASES.put(user.getId(), user);
        log.info("保存用户【user】= {}", user);
        return user;
    }

    /**
     * 获取用户
     * Cacheble注解表示这个方法有了缓存的功能，方法的返回值会被缓存下来，下一次调用该方法前，会去检查是否缓存中已经有值，如果有就直接返回，不调用方法。
     * 如果没有，就调用方法，然后把结果缓存起来。这个注解一般用在查询方法上
     * syn 多个线程尝试用相同的key去缓存拿数据的时候，会是一个同步的操作。cacheNames如果配置了多个，则当sync为true时，取的是第一个Cache，
     * 而没有管剩下的Cache。所以如果你配置了sync为true，只支持配置一个cacheNames，如果配了多个，就会报错
     *
     * @param id key值
     * @return 返回结果
     */
    //@Cacheable(cacheNames = "user", key = "#id", condition = "#id > 1", sync = true)
    @Cacheable(cacheNames = "user", keyGenerator = "myKeyGenerator", condition = "#id > 0", sync = false)
    @Override
    public User get(Long id) {
        // 我们假设从数据库读取
        log.info("查询用户【id】= {}", id);
        return DATABASES.get(id);
    }

    /**
     * 删除
     * CacheEvict 会清空指定缓存。一般用在更新或者删除的方法上。
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
        User user = (User)redisService.get(redisKey);
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
