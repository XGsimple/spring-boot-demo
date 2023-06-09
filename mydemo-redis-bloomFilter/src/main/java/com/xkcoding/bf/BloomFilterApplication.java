package com.xkcoding.bf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * https://blog.csdn.net/qq_35387940/article/details/105700615
 * https://blog.csdn.net/qq_39513430/article/details/107930471
 * 基于redis的布隆过滤，解决缓存击穿问题
 */
@SpringBootApplication
public class BloomFilterApplication {

    public static void main(String[] args) {
        SpringApplication.run(BloomFilterApplication.class, args);
    }

}
