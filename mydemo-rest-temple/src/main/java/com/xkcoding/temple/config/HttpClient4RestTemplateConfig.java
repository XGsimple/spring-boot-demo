package com.xkcoding.temple.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.xkcoding.temple.interceptor.RequestResponseLoggingInterceptor;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author xugangq
 * @description
 * @createTime 2021/3/4 15:15
 */
@Configuration
public class HttpClient4RestTemplateConfig {
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
    public ClientHttpRequestFactory simpleClientHttpRequestFactory(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory HttpsClientRequestFactory = new HttpComponentsClientHttpRequestFactory();
        HttpsClientRequestFactory.setHttpClient(httpClient);
        HttpsClientRequestFactory.setConnectTimeout(15000); // 连接超时
        HttpsClientRequestFactory.setReadTimeout(5000); // 数据读取超时时间

        return HttpsClientRequestFactory;
    }

    /**
     * 创建Http客户端
     *
     * @return Http客户端
     */
    @Bean
    public HttpClient httpClient() {
        return httpClientBuilder().build();
    }

    /**
     * 创建httpClient构建器
     *
     * @return 连接池配置
     */
    private HttpClientBuilder httpClientBuilder() {
        HttpClientBuilder builder = HttpClients.custom();
        builder.setConnectionManagerShared(true);
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(maxTotal);
        connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        builder.setConnectionManager(connectionManager);
        DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(retry, true);
        builder.setRetryHandler(retryHandler);
        List<Header> defaultHeaders = new ArrayList<>();
        defaultHeaders.add(new BasicHeader("Content-Type", "text/html;charset=UTF-8"));
        defaultHeaders.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
        defaultHeaders.add(new BasicHeader("Accept-Language", "zh-CN"));
        defaultHeaders.add(new BasicHeader("Connection", "Keep-Alive"));
        builder.setDefaultHeaders(defaultHeaders);
        //设置https
        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    return true;
                }
            }).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new String[] {"TLSv1", "TLSv1.1", "TLSv1.2"}, null,
                                                                        NoopHostnameVerifier.INSTANCE);
        builder.setSSLSocketFactory(csf);
        return builder;
    }

    class MyCustomSSLSocketFactory extends SSLSocketFactory {

        private final SSLSocketFactory delegate;

        public MyCustomSSLSocketFactory(SSLSocketFactory delegate) {
            this.delegate = delegate;
        }

        // 返回默认启用的密码套件。除非一个列表启用，对SSL连接的握手会使用这些密码套件。
        // 这些默认的服务的最低质量要求保密保护和服务器身份验证
        @Override
        public String[] getDefaultCipherSuites() {
            return delegate.getDefaultCipherSuites();
        }

        // 返回的密码套件可用于SSL连接启用的名字
        @Override
        public String[] getSupportedCipherSuites() {
            return delegate.getSupportedCipherSuites();
        }

        @Override
        public Socket createSocket(final Socket socket, final String host, final int port, final boolean autoClose) throws IOException {
            final Socket underlyingSocket = delegate.createSocket(socket, host, port, autoClose);
            return overrideProtocol(underlyingSocket);
        }

        @Override
        public Socket createSocket(final String host, final int port) throws IOException {
            final Socket underlyingSocket = delegate.createSocket(host, port);
            return overrideProtocol(underlyingSocket);
        }

        @Override
        public Socket createSocket(final String host, final int port, final InetAddress localAddress, final int localPort) throws IOException {
            final Socket underlyingSocket = delegate.createSocket(host, port, localAddress, localPort);
            return overrideProtocol(underlyingSocket);
        }

        @Override
        public Socket createSocket(final InetAddress host, final int port) throws IOException {
            final Socket underlyingSocket = delegate.createSocket(host, port);
            return overrideProtocol(underlyingSocket);
        }

        @Override
        public Socket createSocket(final InetAddress host, final int port, final InetAddress localAddress, final int localPort) throws IOException {
            final Socket underlyingSocket = delegate.createSocket(host, port, localAddress, localPort);
            return overrideProtocol(underlyingSocket);
        }

        private Socket overrideProtocol(final Socket socket) {
            if (!(socket instanceof SSLSocket)) {
                throw new RuntimeException("An instance of SSLSocket is expected");
            }
            //((SSLSocket) socket).setEnabledProtocols(new String[]{"TLSv1.2"});
            ((SSLSocket)socket).setEnabledProtocols(new String[] {"TLSv1", "TLSv1.1", "TLSv1.2"});
            return socket;
        }
    }

}
