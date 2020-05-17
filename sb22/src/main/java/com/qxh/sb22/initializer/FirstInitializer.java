package com.qxh.sb22.initializer;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

@Order(1)
public class FirstInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        // 1. 设置必备属性
        environment.setRequiredProperties("mooc");
//        Map<String, Object> map = new HashMap<>();
//        map.put("key1", "value1");
//        MapPropertySource mapPropertySource = new MapPropertySource("firstInitializer", map);
//        environment.getPropertySources().addLast(mapPropertySource);
//        System.out.println("run firstInitializer");
    }
}
