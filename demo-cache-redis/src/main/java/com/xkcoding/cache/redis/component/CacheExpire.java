package com.xkcoding.cache.redis.component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * cache注解类，可以配置cache失效时间
 */
//此注解作用于方法上
@Target(ElementType.METHOD)
//此注解保留到编译成class文件，加载到jvm中也依然存在
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheExpire {

    /**
     * 失效时间，默认60秒
     */
    long ttl() default 60L;

    /**
     * 失效单位，默认秒
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 今天有效，失效时间是从创建这一刻起，到晚上凌晨12点整，是一个动态的时间
     */
    boolean today() default false;
}

