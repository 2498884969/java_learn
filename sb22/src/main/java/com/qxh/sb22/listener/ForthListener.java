package com.qxh.sb22.listener;

import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;

public class ForthListener implements SmartApplicationListener {
    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
//        isAssignableFrom()方法是判断是否为某个类的父类
//        return ApplicationStartedEvent.class.isAssignableFrom(eventType) || ApplicationPreparedEvent.class.isAssignableFrom(eventType);
        return true;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.err.println(event);
        System.err.println("forth listener");
    }
}
