package com.qxh.autoconfig.bootstrap;


import com.qxh.condition.ConditionalOnSystemProperty;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

public class ConditionalOnSystemPropertyBootstrap {

    @ConditionalOnSystemProperty(name = "user.name", value = "qiangxuhui")
    @Bean
    public String hello(){
        return "hello 强旭辉";
    }

    public static void main(String[] args) {

        ConfigurableApplicationContext context = new SpringApplicationBuilder(ConditionalOnSystemPropertyBootstrap.class)
                .web(WebApplicationType.NONE)
                .run();

        System.out.println(context.getBean("hello", String.class));

        context.close();
    }

}
