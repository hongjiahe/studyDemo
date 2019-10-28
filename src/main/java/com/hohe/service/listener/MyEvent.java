package com.hohe.service.listener;

import org.springframework.context.ApplicationEvent;

/**
 * 自定义事件
 */
public class MyEvent extends ApplicationEvent {
    public MyEvent(Object source) {
        super(source);
    }
}
