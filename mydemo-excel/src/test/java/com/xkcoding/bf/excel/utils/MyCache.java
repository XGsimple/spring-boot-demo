package com.xkcoding.bf.excel.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存
 *
 * @Author XuGang
 * @Date 2024/3/4 16:00
 */
public class MyCache {
    private static final Map<Integer, Object> CACHE = new ConcurrentHashMap<Integer, Object>();

    public static void put(Integer sheetNo, Object data) {
        CACHE.put(sheetNo, data);
    }

    public static Object get(Integer sheetNo) {
        return CACHE.get(sheetNo);
    }
}