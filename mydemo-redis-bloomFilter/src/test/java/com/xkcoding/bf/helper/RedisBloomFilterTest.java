package com.xkcoding.bf.helper;

import com.google.common.hash.BloomFilter;
import com.xkcoding.bf.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@SpringBootTest
class RedisBloomFilterTest {
    @Autowired
    private BloomFilter<CharSequence> bloomFilterStr;

    @Autowired
    private RedisBloomFilter redisBloomFilter;

    @Autowired
    private BloomFilterHelper bloomFilterHelper;

    @Test
    @DisplayName("Google提供的一个Guava布隆过滤器的实现--单机的，使用JDK自带的BitSet来实现")
    void includeByGuavaBloomFilter() {
        for (int i = 0; i < Constant.EXPECTED_INSERTIONS; i++) {
            bloomFilterStr.put("test" + i);
        }
        // 统计误判次数
        int count = 0;
        // 我在数据范围之外的数据，测试相同量的数据，判断错误率是不是符合我们当时设定的错误率
        for (int i = Constant.EXPECTED_INSERTIONS; i < Constant.EXPECTED_INSERTIONS * 2; i++) {
            if (bloomFilterStr.mightContain(String.valueOf(i))) {
                count++;
            }
        }
        log.info("误判元素为==={}", count);
    }

    @Test
    @DisplayName("自定义的布隆过滤器--基于redis的BitMap实现")
    void includeByBloomFilter() {
        String key = "bloom";
        List<String> values = IntStream.range(0, Constant.EXPECTED_INSERTIONS).boxed().map(i -> "test" + i).collect(Collectors.toList());
        redisBloomFilter.batchAddByBloomFilter(bloomFilterHelper, key, values);
        boolean containsValue1 = redisBloomFilter.includeByBloomFilter(bloomFilterHelper, key, "test1");
        boolean containsValue2 = redisBloomFilter.includeByBloomFilter(bloomFilterHelper, key, "test34");
        boolean containsValue3 = redisBloomFilter.includeByBloomFilter(bloomFilterHelper, key, "test1000001");
        System.out.println(containsValue1);
        System.out.println(containsValue2);
        System.out.println(containsValue3);
    }

}
