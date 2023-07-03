package com.xkcoding.bf.helper;

import com.google.common.base.Preconditions;
import com.google.common.hash.BloomFilter;
import com.xkcoding.bf.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xugangq
 * @date 2021/6/17 20:12
 */
@Slf4j
@Component
public class RedisBloomFilter {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private BloomFilter<CharSequence> bloomFilterStr;

    @Autowired
    private BloomFilterHelper defaultBloomFilterHelper;

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
    public <T> void addByBloomFilter(String key, T value) {
        addByBloomFilter(defaultBloomFilterHelper, key, value);
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
    public <T> boolean includeByBloomFilter(String key, T value) {
        return includeByBloomFilter(defaultBloomFilterHelper, key, value);
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
    public <T> void batchAddByBloomFilter(String key, List<T> values) {
        batchAddByBloomFilter(defaultBloomFilterHelper, key, values);
    }

    /**
     * 批量根据给定的布隆过滤器添加值--基于redis的BitMap实现
     */
    public <T> void batchAddByBloomFilter(BloomFilterHelper<T> bloomFilterHelper, String key, List<T> values) {
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        StopWatch stopWatch = new StopWatch("batchAddByBloomFilter");
        stopWatch.start();
        RedisSerializer<String> strSerializer = redisTemplate.getStringSerializer();
        byte[] rawKey = strSerializer.serialize(key);
        List<List<T>> subList = getSubList(values, Constant.PIPLINE_LIST_LEN);
        //基于RedisCallback
        //        redisTemplate.executePipelined((RedisCallback<String>)connection -> {
        //            log.info("pipeline0 = {}", connection.isPipelined());
        //            connection.openPipeline();
        //            log.info("pipeline1 = {}", connection.isPipelined());
        //            subList.forEach(theList -> {
        //                theList.forEach(v -> {
        //                    int[] offset = bloomFilterHelper.murmurHashOffset(v);
        //                    for (int i : offset) {
        //                        //基于原生connection操作，如果使用  redisTemplate.opsForValue().setBit(key, i, true);没有用
        //                        connection.setBit(rawKey, i, true);
        //                    }
        //                });
        //            });
        //            connection.closePipeline();
        //            return null;
        //        });
        //基于SessionCallback
        redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                ValueOperations<String, Object> valueOperations = (ValueOperations<String, Object>)operations.opsForValue();
                subList.forEach(theList -> {
                    theList.forEach(v -> {
                        int[] offset = bloomFilterHelper.murmurHashOffset(v);
                        for (int i : offset) {
                            //基于原生connection操作，如果使用  redisTemplate.opsForValue().setBit(key, i, true);没有用
                            valueOperations.setBit(key, i, true);
                        }
                    });
                });
                return null;
            }
        });

        stopWatch.stop();
        //10万的数据，RedisCallback大概10秒，SessionCallback大概7秒
        log.info("cost {} s in bytes pipeline", stopWatch.getTotalTimeSeconds());
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
