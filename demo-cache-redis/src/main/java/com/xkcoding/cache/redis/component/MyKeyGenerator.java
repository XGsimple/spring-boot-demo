package com.xkcoding.cache.redis.component;

import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xugangq
 * @date 2021/6/29 14:32
 */
public class MyKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        return target.getClass().getName() + method.getName() + Stream.of(params).map(Object::toString).collect(Collectors.joining(","));
    }
}
