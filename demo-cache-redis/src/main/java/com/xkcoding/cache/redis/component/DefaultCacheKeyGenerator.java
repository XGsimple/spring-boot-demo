package com.xkcoding.cache.redis.component;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * @author xugangq
 * @date 2021/6/29 14:32
 */
public class DefaultCacheKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        return target.getClass().getSimpleName() + ":" + method.getName() + "(" +
               JSONUtil.toJsonStr(params, new JSONConfig().setIgnoreNullValue(true)) + ")";
    }
}
