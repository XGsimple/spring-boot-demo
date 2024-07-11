package com.xkcoding.cache.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.xkcoding.cache.redis.component.DefaultCacheKeyGenerator;
import com.xkcoding.cache.redis.component.JsonConverters;
import com.xkcoding.cache.redis.component.RedisExpireCacheResolver;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
        //使用fastjson序列化
        //FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);
        RedisSerializer<Object> serializer = redisSerializer();
        RedisSerializer<String> strSerializer = RedisSerializer.string();
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(strSerializer);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(strSerializer);
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
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
                                                                                 .serializeKeysWith(
                                                                                     RedisSerializationContext.SerializationPair.fromSerializer(
                                                                                         new StringRedisSerializer()))
                                                                                 .serializeValuesWith(
                                                                                     RedisSerializationContext.SerializationPair.fromSerializer(
                                                                                         redisSerializer()))
                                                                                 //变双冒号为单冒号
                                                                                 .computePrefixWith(name -> name + ":")
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
        //添加时间日期格式的处理
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // 添加对自定义日期类型解析的解析器
        javaTimeModule.addSerializer(LocalDateTime.class, JsonConverters.localDateTimeSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, JsonConverters.localDateTimeDeserializer());
        javaTimeModule.addSerializer(LocalDate.class, JsonConverters.localDateSerializer());
        javaTimeModule.addDeserializer(LocalDate.class, JsonConverters.localDateDeserializer());
        objectMapper.registerModule(javaTimeModule);
        serializer.setObjectMapper(objectMapper);
        return serializer;
    }

    /**
     * spring cache 自定义key 生成器
     *
     * @return
     */
    @Bean
    public KeyGenerator defaultCacheKeyGenerator() {
        return new DefaultCacheKeyGenerator();
    }

    /**
     * 注册自定义的缓存处理类
     * cacheManager 为使用的缓存管理器
     * 使用CacheResolver来接收，它属于父接口类
     * 使用方式，指明处理器: @Cacheable(cacheNames = "yyyy",key = "#root.args[0]",cacheResolver = "redisExpireCacheResolver")
     */
    @Bean
    public CacheResolver redisExpireCacheResolver(CacheManager cacheManager) {
        return new RedisExpireCacheResolver(cacheManager);
    }
}
