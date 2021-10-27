package com.xkcoding.temple.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.xkcoding.temple.interceptor.RequestResponseLoggingInterceptor;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author xugangq
 * @date 2021/10/26 14:41
 */
//@Configuration
public class OkHttp4RestTemplateConfig {
    /**
     * 整个连接池的并发
     */
    @Value("${spring.rest-service.pool.max:50}")
    private int maxTotal;
    /**
     * 每个主机的并发
     */
    @Value("${spring.rest-service.pool.max-perroute:50}")
    private int defaultMaxPerRoute;
    /**
     * 重试次数
     */
    @Value("${spring.rest-service.call.retry:3}")
    private int retry;
    /**
     * 连接超时时长
     */
    @Value("${spring.rest-service.timeout.connection:60000}")
    private int conntecionTimeout;
    /**
     * 读取数据超时时长
     */
    @Value("${spring.rest-service.timeout.read:60000}")
    private int readTimeout;

    /**
     * 创建rest调用模板
     *
     * @return rest调用模板
     */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(factory));
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.add(new FastJsonHttpMessageConverter());
        restTemplate.setMessageConverters(messageConverters);
        restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory(OkHttpClient okHttpClient) {
        OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory = new OkHttp3ClientHttpRequestFactory(okHttpClient);
        okHttp3ClientHttpRequestFactory.setConnectTimeout(conntecionTimeout);
        okHttp3ClientHttpRequestFactory.setReadTimeout(readTimeout);
        okHttp3ClientHttpRequestFactory.setWriteTimeout(readTimeout);
        return okHttp3ClientHttpRequestFactory;
    }

    @Bean
    public ConnectionPool connectionPool() {
        // 设置连接池参数，最大空闲连接数200，空闲连接存活时间10s
        ConnectionPool connectionPool = new ConnectionPool(maxTotal, 10, TimeUnit.SECONDS);
        return connectionPool;
    }

    /**
     * 创建Http客户端
     *
     * @return Http客户端
     */
    @Bean
    public OkHttpClient okHttpClient(ConnectionPool connectionPool) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
            //  设置连接超时
            .connectTimeout(5 * 1000, TimeUnit.MILLISECONDS)
            //  设置读取超时
            .readTimeout(5 * 1000, TimeUnit.MILLISECONDS)
            //  设置写入超时
            .writeTimeout(5 * 1000, TimeUnit.MILLISECONDS)
            //  设置连接失败重试
            .retryOnConnectionFailure(true)
            //  设置最大连接数 和 保持连接时间
            .connectionPool(connectionPool).build();
        return okHttpClient;
    }

}
