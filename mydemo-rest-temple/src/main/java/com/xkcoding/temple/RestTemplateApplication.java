package com.xkcoding.temple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 掌握 Spring 之 RestTemplate
 * https://juejin.cn/post/6844903842065154061
 *
 * RestTemplate 定义了 36 个与 REST 资源交互的方法，其中的大多数都对应于 HTTP 的方法。
 * 注意：
 * 严格来说只有 11 个独立的方法，其中有 10 个有三种重载形式，而第 11 个则重载了 6 次，这样一共形成了 36 个方法。
 * 实际上，由于 Post 操作的非幂等性，它几乎可以代替其他的 CRUD 操作。
 * delete()：这个方法是在特定的 URL 上对资源执行 HTTP DELETE 操作
 * exchange()：在 URL 上执行特定的 HTTP 方法，返回包含对象的 ResponseEntity，这个对象是从响应体中映射得到的
 * execute()：在 URL 上执行特定的 HTTP 方法，返回一个从响应体映射得到的对象
 * getForEntity()：发送一个 HTTP GET 请求，返回的 ResponseEntity 包含了响应体所映射成的对象
 * getForObject()：发送一个 HTTP GET 请求，返回的请求体将映射为一个对象
 * postForEntity()：POST 数据到一个 URL，返回包含一个对象的 ResponseEntity，这个对象是从响应体中映射得到的
 * postForObject()：POST 数据到一个 URL，返回根据响应体匹配形成的对象
 * headForHeaders()：发送 HTTP HEAD 请求，返回包含特定资源 URL 的 HTTP 头
 * optionsForAllow()：发送 HTTP OPTIONS 请求，返回对特定 URL 的 Allow 头信息
 * postForLocation()：POST 数据到一个 URL，返回新创建资源的 URL
 * put()：PUT 资源到特定的 URL
 */
@SpringBootApplication
public class RestTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestTemplateApplication.class, args);
    }

}
