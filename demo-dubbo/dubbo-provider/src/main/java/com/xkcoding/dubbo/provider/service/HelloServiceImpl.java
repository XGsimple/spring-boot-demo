package com.xkcoding.dubbo.provider.service;

import com.xkcoding.dubbo.common.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Hello服务实现
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-12-25 16:58
 */
@Slf4j
@Component
@DubboService
public class HelloServiceImpl implements HelloService {
    /**
     * 问好
     *
     * @param name 姓名
     * @return 问好
     */
    @Override
    public String sayHello(String name) {
        log.info("someone is calling me......");
        return "say hello to: " + name;
    }
}
