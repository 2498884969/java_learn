package com.qxh.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashSet;
import java.util.Set;

//@SpringBootApplication
public class SpringApplicationBootstrap {

    @SpringBootApplication
    public static class ApplicationConfiguration{}

    public static void main(String[] args) {

//        SpringApplication.run(ApplicationConfiguration.class, args);

        Set<String> strings = new HashSet<>();
        strings.add(ApplicationConfiguration.class.getName());
        SpringApplication application = new SpringApplication();
        application.setWebApplicationType(WebApplicationType.NONE);
        application.setSources(strings);
        ConfigurableApplicationContext ctx = application.run(args);
//        ctx.close();


    }

}
