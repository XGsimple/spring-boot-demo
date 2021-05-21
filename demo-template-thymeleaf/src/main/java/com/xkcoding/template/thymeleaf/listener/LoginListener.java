package com.xkcoding.template.thymeleaf.listener;

import com.xkcoding.template.thymeleaf.controller.UserController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;

import java.util.Date;

/**
 * 登录事件监听者
 *
 * @author xugangq
 * @date 2021/5/21 15:11
 */
@Slf4j
public class LoginListener implements GenericApplicationListener {
    private static final Class<?>[] EVENT_TYPES = {LoginEvent.class};
    private static final Class<?>[] SOURCE_TYPES = {UserController.class};

    @Override
    public boolean supportsEventType(ResolvableType resolvableType) {
        return isAssignableFrom(resolvableType.getRawClass(), EVENT_TYPES);
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return isAssignableFrom(sourceType, SOURCE_TYPES);
    }

    private boolean isAssignableFrom(Class<?> type, Class<?>... supportedTypes) {
        if (type != null) {
            for (Class<?> supportedType : supportedTypes) {
                if (supportedType.isAssignableFrom(type)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof IndexEvent) {
            onIndexEvent((IndexEvent)event);
        } else if (event instanceof LoginEvent) {
            onLoginEvent((LoginEvent)event);
        }
    }

    private void onIndexEvent(IndexEvent event) {
        print("进入主页");
    }

    private void onLoginEvent(LoginEvent event) {
        print("登录了");

    }

    private void print(String msg) {
        Date now = new Date();
        System.out.println("==========================="+Thread.currentThread().getName()+"----"+now+"===="+msg+"==========================");

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
