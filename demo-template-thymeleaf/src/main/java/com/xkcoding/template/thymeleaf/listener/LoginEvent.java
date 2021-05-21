package com.xkcoding.template.thymeleaf.listener;

import org.springframework.context.ApplicationEvent;

/**
 * 登录事件
 *
 * @author xugangq
 * @date 2021/5/21 15:02
 */
public class LoginEvent extends ApplicationEvent {
    public LoginEvent(Object source) {
        super(source);
    }
}
