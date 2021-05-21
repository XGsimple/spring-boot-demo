package com.xkcoding.template.thymeleaf.listener;

import org.springframework.context.ApplicationEvent;

/**
 * 进入主页的事件
 *
 * @author xugangq
 * @date 2021/5/21 15:02
 */
public class IndexEvent extends ApplicationEvent {
    public IndexEvent(Object source) {
        super(source);
    }
}
