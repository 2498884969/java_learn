package com.qxh.autoconfig.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class HelloWorldConfiguration {

    @Bean
    public String hello(){
        return "hello world 2020";
    }


}
