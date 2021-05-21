package com.xkcoding.template.thymeleaf.listener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**用于配置事件分发器
 *
 *
 * @author xugangq
 * @date 2021/5/21 15:28
 */
@Configuration
public class MyListerConfig {
    @Bean
    public SimpleApplicationEventMulticaster mySimpleApplicationEventMulticaster() {
        SimpleApplicationEventMulticaster multicaster = new SimpleApplicationEventMulticaster();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        multicaster.setTaskExecutor(executorService);
        multicaster.addApplicationListener(new LoginListener());
        return multicaster;
    }
}
