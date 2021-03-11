package com.xkcoding.temple;

import com.alibaba.fastjson.TypeReference;
import com.xkcoding.temple.bean.Product;
import com.xkcoding.temple.bean.ResultMessage;
import com.xkcoding.temple.config.RestTemplateConfig;
import com.xkcoding.temple.interceptor.UserAgentInterceptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;

import static org.springframework.util.Assert.isTrue;

/**
 * @author One
 * @Description RestTemplate 请求测试类
 * @date 2019/05/09
 */
@RunWith(SpringRunner.class)
@RestClientTest(value = {RestTemplateConfig.class})
public class RestTemplateTests {
    @Autowired
    RestTemplate restTemplate;
/*    RestTemplate restTemplate = new RestTemplate();
    RestTemplate customRestTemplate = null;

    @Before
    public void setup() {
        customRestTemplate = new RestTemplate(getClientHttpRequestFactory());
    }

    private SimpleClientHttpRequestFactory getClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory clientHttpRequestFactory
            = new SimpleClientHttpRequestFactory();
        // 连接超时设置 10s
        clientHttpRequestFactory.setConnectTimeout(10_000);

        // 读取超时设置 10s
        clientHttpRequestFactory.setReadTimeout(10_000);
        return clientHttpRequestFactory;
    }*/


    @Test
    public void testGet_product1() {
        String url = "http://localhost:8081/product/get_product1";
        String result = restTemplate.getForObject(url, String.class);
        System.out.println("get_product1返回结果：" + result);
        Assert.hasText(result, "get_product1返回结果为空");

        Product product = restTemplate.getForObject(url, Product.class);
        System.out.println("get_product1返回结果：" + product);
        Assert.notNull(product, "get_product1返回结果为空");

        ResponseEntity<Product> responseEntity = restTemplate.getForEntity(url, Product.class);
        System.out.println("get_product1返回结果：" + responseEntity);
        Assert.isTrue(responseEntity.getStatusCode().equals(HttpStatus.OK), "get_product1响应不成功");

//        MultiValueMap header = new LinkedMultiValueMap();
//        header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.setAcceptCharset(Collections.singletonList(Charset.forName("UTF-8")));
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Product> exchangeResult = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Product.class);
        System.out.println("get_product1返回结果：" + exchangeResult);
        Assert.isTrue(exchangeResult.getStatusCode().equals(HttpStatus.OK), "get_product1响应不成功");

        String executeResult = restTemplate.execute(url, HttpMethod.GET, request -> {
            request.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        }, (clientHttpResponse) -> {
            InputStream body = clientHttpResponse.getBody();
            return StreamUtils.copyToString(body, Charset.defaultCharset());
        });
        System.out.println("get_product1返回结果：" + executeResult);
        Assert.hasText(executeResult, "get_product1返回结果为空");
    }

    //url带参数的Get请求
    @Test
    public void testGet_product2() {
        String url = "http://localhost:8081/product/get_product2?id={id}";
        ResponseEntity<Product> responseEntity = restTemplate.getForEntity(url, Product.class, 101);
        System.out.println(responseEntity);
        isTrue(responseEntity.getStatusCode().equals(HttpStatus.OK), "get_product2 请求不成功");
        Assert.notNull(responseEntity.getBody().getId(), "get_product2  传递参数不成功");

        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("id", 101);
        Product result = restTemplate.getForObject(url, Product.class, uriVariables);
        System.out.println(result);
        Assert.notNull(result.getId(), "get_product2  传递参数不成功");
    }


    //header中带有cookie的Get请求
    @Test
    public void testGet_product3() {
        String url = "http://localhost:8081/product/get_product2?id={id}";
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("id", 101);
        //请求参数
        //请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        //===设置Cookie
        List<String> cookieList = new ArrayList<>();
        String token= "xxxxxxxxx";
        cookieList.add("token=" + token);
        headers.put(HttpHeaders.COOKIE, cookieList); //将cookie放入header
        HttpEntity<Object> request = new HttpEntity<>(headers);
        ResponseEntity<ResultMessage> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, ResultMessage.class, uriVariables);
    }

    //application/x-www-form-urlencoded
    @Test
    public void testPost_product1() {
        String url = "http://localhost:8081/product/post_product1";
        MultiValueMap<String, String> header = new LinkedMultiValueMap();
        header.add(HttpHeaders.CONTENT_TYPE, (MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        Product product = new Product(201, "Macbook", BigDecimal.valueOf(10000));
        String productStr = "id=" + product.getId() + "&name=" + product.getName() + "&price=" + product.getPrice();
        HttpEntity<String> request = new HttpEntity<>(productStr, header);
        ResponseEntity<String> exchangeResult = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        System.out.println("post_product1: " + exchangeResult);
        Assert.isTrue(exchangeResult.getStatusCode().equals(HttpStatus.OK), "post_product1 请求不成功");

        MultiValueMap<String, Object> map = new LinkedMultiValueMap();
        map.add("id", (product.getId()));
        map.add("name", (product.getName()));
        map.add("price", (product.getPrice()));
        HttpEntity<MultiValueMap> request2 = new HttpEntity<>(map, header);
        ResponseEntity<String> exchangeResult2 = restTemplate.exchange(url, HttpMethod.POST, request2, String.class);
        System.out.println("post_product1： " + exchangeResult2);
        Assert.isTrue(exchangeResult.getStatusCode().equals(HttpStatus.OK), "post_product1 请求不成功");
    }

    //发送 Content-Type 为 application/json 的 POST 请求：
    @Test
    public void testPost_product2() {
        String url = "http://localhost:8081/product/post_product2";
        MultiValueMap<String, String> header = new LinkedMultiValueMap();
        header.put(HttpHeaders.CONTENT_TYPE, Arrays.asList(MediaType.APPLICATION_JSON_VALUE));
        header.put(HttpHeaders.ACCEPT, Arrays.asList(MediaType.APPLICATION_JSON_VALUE));
        HttpEntity<Product> request = new HttpEntity<>(new Product(2, "Macbook", BigDecimal.valueOf(10000)), header);
        ResponseEntity<String> exchangeResult = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        System.out.println("post_product2: " + exchangeResult.getBody());
        Assert.isTrue(exchangeResult.getStatusCode().equals(HttpStatus.OK), "post_product2 请求不成功");
    }

    @Test
    public void testDelete() {
        String url = "http://localhost:8081/product/delete/{id}";
        restTemplate.delete(url, 101);
    }

    @Test
    public void testPut() {
        String url = "http://localhost:8081/product/update";
        Map<String, ?> variables = new HashMap<>();
        MultiValueMap<String, String> header = new LinkedMultiValueMap();
        header.put(HttpHeaders.CONTENT_TYPE, Arrays.asList(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        Product product = new Product(101, "iWatch", BigDecimal.valueOf(2333));
        String productStr = "id=" + product.getId() + "&name=" + product.getName() + "&price=" + product.getPrice();
        HttpEntity<String> request = new HttpEntity<>(productStr, header);
        restTemplate.put(url, request);
    }

    @Test
    public void testUploadFile() {
        String url = "http://localhost:8081/product/upload";
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        Object file = new FileSystemResource(new File("/Users/One/Desktop/b.txt"));
        body.add("file", file);

        MultiValueMap<String, String> header = new LinkedMultiValueMap();
        header.put(HttpHeaders.CONTENT_TYPE, Arrays.asList(MediaType.MULTIPART_FORM_DATA_VALUE));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, header);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        System.out.println("upload: " + responseEntity);
        Assert.isTrue(responseEntity.getStatusCode().equals(HttpStatus.OK), "upload 请求不成功");
    }


    /**
     * 通用方式设置请求头
     */
    @Test
    public void testGetHeader() {
        /**
         * RestTemplate设置使用请求头的拦截器
         */
        restTemplate.setInterceptors(Collections.singletonList(new UserAgentInterceptor()));

        /**
         * 正常的发送请求
         */
        String result = restTemplate.getForObject("http://localhost:8080/testRestGet?username=zhangsan", String.class);

        System.out.println(result);
    }

    /**
     * Post方式设置请求头
     */
    @Test
    public void testPostHeader() {
        //1. 设置请求头参数
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
        //2. 模拟表单参数 请求体携带参数
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("username", "zhangsan");
        //3. 封装HttpEntity对象
        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<MultiValueMap>(requestBody, requestHeaders);
        RestTemplate restTemplate = new RestTemplate();

        //4. 发送Post请求
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8080/testRestPost", requestEntity, String.class);
        System.out.println(responseEntity.getBody());
    }

    /**
     * 测试Post请求
     */
    @Test
    public void testPostForObject() {
        /**
         * postForObject 返回值为响应的数据
         * 参数1 要请求地址的url
         * 参数2 通过LinkedMultiValueMap对象封装请求参数  模拟表单参数，封装在请求体中
         * 参数3 响应数据的类型
         */
        LinkedMultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.set("username", "zhangsan");

        String result = restTemplate.postForObject("http://localhost:8080/testRestPost", request, String.class);

        System.out.println(result);

        /**
         * Post请求的时候同样也可以进行参数拼接，使用方式和Get一样
         * 示例如下,通过map封装数据，利用占位符的方式可以将参数拼接到url上
         * 和Get请求url拼接一样
         */
        Map map = new HashMap();
        map.put("password", "123456");

        String result2 = restTemplate.postForObject("http://localhost:8080/testRestPost?password={password}", request,
            String.class, map);

        /**
         * postForLocation 这个API和前两个都不一样
         *
         * 登录or注册都是post请求，而这些操作完成之后呢？大部分都是跳转到别的页面去了，这种场景下，就可以使用 postForLocation 了，提交数据，并获取返回的URI
         * 响应参数要跳转的地址
         */
        URI uri = restTemplate.postForLocation("http://localhost:8080/testRestPostLocation", request);
        System.out.println("postForLocation请求到的地址为：" + uri);
    }



}
