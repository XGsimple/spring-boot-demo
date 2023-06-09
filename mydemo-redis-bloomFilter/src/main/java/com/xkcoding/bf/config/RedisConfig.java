package com.xkcoding.bf.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import com.xkcoding.bf.constant.Constant;
import com.xkcoding.bf.helper.BloomFilterHelper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * <p>
 * redis配置
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-11-15 16:41
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    //    /**
    //     * 默认情况下的模板只能支持RedisTemplate<String, String>，也就是只能存入字符串，因此支持序列化
    //     */
    //    @Bean
    //    public RedisTemplate<String, Serializable> redisCacheTemplate(LettuceConnectionFactory redisConnectionFactory) {
    //        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
    //        template.setKeySerializer(new StringRedisSerializer());
    //        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    //        template.setConnectionFactory(redisConnectionFactory);
    //        return template;
    //    }
    //
    //    /**
    //     * 配置使用注解的时候缓存配置，默认是序列化反序列化的形式，加上此配置则为 json 形式
    //     */
    //    @Bean
    //    public CacheManager cacheManager(RedisConnectionFactory factory) {
    //        // 配置序列化
    //        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
    //        RedisCacheConfiguration redisCacheConfiguration = config
    //            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
    //            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    //
    //        return RedisCacheManager.builder(factory).cacheDefaults(redisCacheConfiguration).build();
    //    }

    @Bean
    public RedisTemplate<String, Object> redisCacheTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisSerializer<Object> serializer = redisSerializer();
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 配置使用注解的时候缓存配置，默认是序列化反序列化的形式，加上此配置则为 json 形式
     * CacheProperties 缓存配置属性类
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        //设置Redis缓存有效期为1天
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                                                                                 .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
                                                                                     new StringRedisSerializer()))
                                                                                 .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                                                                                     redisSerializer()))
                                                                                 .entryTtl(Duration.ofDays(1));
        return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
    }

    @Bean
    public RedisSerializer<Object> redisSerializer() {
        //创建JSON序列化器
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(objectMapper);
        return serializer;
    }

    /**
     * 初始化自定义的布隆过滤器，放入到spring容器里面
     *
     * @return
     */
    @Bean
    public BloomFilterHelper<String> initBloomFilterHelper() {
        return new BloomFilterHelper<>((Funnel<String>)(from, into) -> into.putString(from, Charsets.UTF_8), Constant.EXPECTED_INSERTIONS, Constant.FPP);
    }

    /**
     * 基于Guava实现的BloomFilter
     *
     * @return
     */
    @Bean
    public BloomFilter<CharSequence> bloomFilterStr() {
        return BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), Constant.EXPECTED_INSERTIONS, Constant.FPP);
    }
}
