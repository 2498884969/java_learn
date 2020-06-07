package com.qxh.application.context;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

public class AfterHelloWorldApplicationContextInitializer<C extends
        ConfigurableApplicationContext> implements ApplicationContextInitializer<C>, Ordered {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        System.out.println("after applicationContext.id = " + applicationContext.getId());
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
