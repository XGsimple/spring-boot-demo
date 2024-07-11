package com.xkcoding.cache.redis.component;

import cn.hutool.core.util.StrUtil;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.*;

/**
 * 使用继承的方式对cache处理器进行扩展
 * <p>
 * 自定义缓存失效: https://juejin.cn/post/7078230083118301220#heading-8
 * 自定义CacheResolver动态设置失效时间: https://blog.csdn.net/ZHANGLIZENG/article/details/128724153
 */
public class RedisExpireCacheResolver extends SimpleCacheResolver {

    public RedisExpireCacheResolver(CacheManager cacheManager) {
        super(cacheManager);
    }

    //重写处理cache的方法
    @Override
    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
        //直接参考父父类AbstractCacheResolver的resolveCaches方法
        //获取当前注解中的缓存名，通过父类进行获取
        Collection<String> cacheNames = getCacheNames(context);
        if (cacheNames == null) {
            return Collections.emptyList();
        }
        //是否不走缓存
        boolean isNoCache = Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                                    .map(theRequestAttributes -> {
                                        // 获取当前请求的属性
                                        ServletRequestAttributes attributes = (ServletRequestAttributes)theRequestAttributes;
                                        // 从属性中获取HttpServletRequest对象
                                        HttpServletRequest request = attributes.getRequest();
                                        // 从请求中获取名为"redis-cache-tll"的header值
                                        String redisCacheTll = request.getHeader("no-redis-cache");
                                        return StrUtil.isBlank(redisCacheTll) ? null : Boolean.valueOf(redisCacheTll);
                                    })
                                    .orElse(Boolean.FALSE);
        Collection<Cache> result = new ArrayList<>(cacheNames.size());
        for (String cacheName : cacheNames) {
            //通过缓存名从缓存管理器中获取到缓存对象
            Cache cache = getCacheManager().getCache(cacheName);
            if (cache == null) {
                throw new IllegalArgumentException(
                    "Cannot find cache named '" + cacheName + "' for " + context.getOperation());
            } else if (isNoCache && cache instanceof RedisCache) {//基于no-redis-cache，不走缓存
                cache = new SydRedisCache((RedisCache)cache);
            } else {//基于@CacheExpire注解，动态设置缓存过期时间
                //跟AbstractCacheResolver的resolveCaches方法比较，只有这里新增解析注解，反射替换RedisCacheConfiguration的处理
                parseCacheExpire(cache, context);
            }
            result.add(cache);
        }
        return result;
    }

    /**
     * 通过反射替换cache中的RedisCacheConfiguration类已经设置好的过期时间等的值
     * 解析自定义注解CacheExpire，从注解中获取设置的过期时间，给RedisCacheConfiguration重新赋值
     */
    private void parseCacheExpire(Cache cache, CacheOperationInvocationContext<?> context) {
        Method method = context.getMethod();
        //判断方法是否包含过期时间注解
        if (method.isAnnotationPresent(CacheExpire.class)) {  //包含CacheExpire注解再处理
            //获取到注解
            CacheExpire cacheExpire = method.getAnnotation(CacheExpire.class);
            Duration duration = null;

            if (cacheExpire.today()) {//当配置了过期时间为今天，则计算从这一刻到凌晨12点还有多少时间
                duration = Duration.ofSeconds(getSecondsNextEarlyMorning());
            } else {//过期时间为用户自己配置，则根据配置的来创建Duration
                duration = Duration.ofSeconds(cacheExpire.unit().toSeconds(cacheExpire.ttl()));
            }
            //转成RedisCache 这个时候cacheConfig是空的，也就让反射有了可乘之机
            RedisCache redisCache = (RedisCache)cache;
            //获取cache里面的RedisCacheConfiguration
            RedisCacheConfiguration cacheConfiguration = redisCache.getCacheConfiguration();
            //新生成一个configuration
            RedisCacheConfiguration cacheConfig = cacheConfiguration;
            //参数需要对应修改
            cacheConfig = cacheConfig.entryTtl(duration);
            //通过反射获取到类型为RedisCacheConfiguration的字段cacheConfig
            Field field = ReflectionUtils.findField(RedisCache.class, "cacheConfig", RedisCacheConfiguration.class);
            //设置可以访问被private修饰的字段值
            field.setAccessible(true);
            //重新设置替换RedisCacheConfiguration
            ReflectionUtils.setField(field, redisCache, cacheConfig);
        }
    }

    //获取当前时间到第二天凌晨的秒数，用于设置redis失效时间为当天
    private Long getSecondsNextEarlyMorning() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }

}
