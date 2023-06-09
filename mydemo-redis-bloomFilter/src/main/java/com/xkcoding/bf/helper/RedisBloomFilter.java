package com.xkcoding.bf.helper;

import com.google.common.base.Preconditions;
import com.google.common.hash.BloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author xugangq
 * @date 2021/6/17 20:12
 */

@Service
public class RedisBloomFilter {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private BloomFilter<CharSequence> bloomFilterStr;

    /**
     * 根据给定的Guava布隆过滤器添加值
     */
    public void addByGuavaBloomFilter(String value) {
        bloomFilterStr.put(value);
    }

    /**
     * 根据给定的Guava布隆过滤器判断值是否存在
     */
    public boolean includeByGuavaBloomFilter(String value) {
        return bloomFilterStr.mightContain(value);
    }

    /**
     * 根据给定的布隆过滤器添加值
     */
    public <T> void addByBloomFilter(BloomFilterHelper<T> bloomFilterHelper, String key, T value) {
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            System.out.println("key : " + key + " " + "value : " + i);
            redisTemplate.opsForValue().setBit(key, i, true);
        }
    }

    /**
     * 根据给定的布隆过滤器判断值是否存在
     */
    public <T> boolean includeByBloomFilter(BloomFilterHelper<T> bloomFilterHelper, String key, T value) {
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            System.out.println("key : " + key + " " + "value : " + i);
            if (!redisTemplate.opsForValue().getBit(key, i)) {
                return false;
            }
        }
        return true;
    }

}
