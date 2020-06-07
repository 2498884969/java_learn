package com.qxh.application;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringApplicationEventBootstrap {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext configApplicationContext = new AnnotationConfigApplicationContext();

        configApplicationContext.addApplicationListener(
                event -> {
                    System.out.println("监听到事件："+event);
                }
        );

        configApplicationContext.refresh();

        configApplicationContext.publishEvent("hello world");
        configApplicationContext.publishEvent("2020");
        configApplicationContext.publishEvent(new ApplicationEvent("强旭辉") {
        });

        configApplicationContext.close();
    }

}
