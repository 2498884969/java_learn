package com.qxh.autoconfig.bootstrap;

import com.qxh.autoconfig.annotation.EnableHelloWorld;
import com.qxh.autoconfig.config.HelloWorldConfiguration;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@EnableHelloWorld
public class EnableHelloWorldBootstrap {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(EnableHelloWorldBootstrap.class)
                .web(WebApplicationType.NONE)
                .run(args);

        System.out.println(
                context.getBean(HelloWorldConfiguration.class)
        );

        System.out.println(
                context.getBean("hello", String.class)
        );

        context.close();
    }

}
