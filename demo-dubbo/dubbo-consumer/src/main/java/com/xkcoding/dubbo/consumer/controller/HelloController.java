package com.xkcoding.dubbo.consumer.controller;

import com.xkcoding.dubbo.common.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * Hello服务API
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-12-25 17:22
 */
@RestController
@Slf4j
public class HelloController {
    @DubboReference(protocol = "dubbo", loadbalance = "random")
    private HelloService helloService;

    @GetMapping("/sayHello")
    public String sayHello(@RequestParam(defaultValue = "xkcoding") String name) {
        log.info("i'm ready to call someone......");
        return helloService.sayHello(name);
    }
}
