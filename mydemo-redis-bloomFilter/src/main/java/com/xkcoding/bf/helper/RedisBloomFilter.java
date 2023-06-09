package com.xkcoding.bf.helper;

import com.google.common.base.Preconditions;
import com.google.common.hash.BloomFilter;
import com.xkcoding.bf.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xugangq
 * @date 2021/6/17 20:12
 */
@Slf4j
@Service
public class RedisBloomFilter {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private BloomFilter<CharSequence> bloomFilterStr;

    /**
     * 根据给定的Guava布隆过滤器添加值--单机的，使用JDK自带的BitSet来实现
     */
    public void addByGuavaBloomFilter(String value) {
        bloomFilterStr.put(value);
    }

    /**
     * 根据给定的Guava布隆过滤器判断值是否存在--单机的，使用JDK自带的BitSet来实现
     */
    public boolean includeByGuavaBloomFilter(String value) {
        return bloomFilterStr.mightContain(value);
    }

    /**
     * 根据给定的布隆过滤器添加值--基于redis的BitMap实现
     */
    public <T> void addByBloomFilter(BloomFilterHelper<T> bloomFilterHelper, String key, T value) {
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            //            System.out.println("key : " + key + " " + "value : " + i);
            redisTemplate.opsForValue().setBit(key, i, true);
        }
    }

    /**
     * 根据给定的布隆过滤器判断值是否存在--基于redis的BitMap实现
     */
    public <T> boolean includeByBloomFilter(BloomFilterHelper<T> bloomFilterHelper, String key, T value) {
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            //            System.out.println("key : " + key + " " + "value : " + i);
            if (!redisTemplate.opsForValue().getBit(key, i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 批量根据给定的布隆过滤器添加值--基于redis的BitMap实现
     */
    public <T> void batchAddByBloomFilter(BloomFilterHelper<T> bloomFilterHelper, String key, List<T> values) {
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        RedisSerializer<String> strSerializer = redisTemplate.getStringSerializer();
        byte[] rawKey = strSerializer.serialize(key);
        List<List<T>> subList = getSubList(values, Constant.PIPLINE_LIST_LEN);
        redisTemplate.executePipelined((RedisCallback<String>)connection -> {
            log.info("pipeline0 = {}", connection.isPipelined());
            connection.openPipeline();
            log.info("pipeline1 = {}", connection.isPipelined());
            subList.forEach(theList -> {
                theList.forEach(v -> {
                    int[] offset = bloomFilterHelper.murmurHashOffset(v);
                    for (int i : offset) {
                        //基于原生connection操作，如果使用  redisTemplate.opsForValue().setBit(key, i, true);没用用
                        connection.setBit(rawKey, i, true);
                    }
                });
            });
            connection.closePipeline();
            return null;
        });
    }

    /**
     * 删除过滤key
     *
     * @param key
     */
    public void removeByBloomFilter(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 拆分list队列
     *
     * @param list
     * @param len
     * @param <T>
     * @return
     */
    private <T> List<List<T>> getSubList(List<T> list, Integer len) {
        if (list == null || list.size() == 0 || len < 1) {
            return null;
        }
        List<List<T>> resultList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i % len == 0) {
                int count = i / len;
                List<T> subList = list.stream().limit((count + 1) * len).skip(count * len).collect(Collectors.toList());
                resultList.add(subList);
            }
        }
        return resultList;
    }
}
