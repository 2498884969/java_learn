package com.qxh.webmvc.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.qxh.webmvc")
public class SpringBootMvcBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMvcBootstrap.class, args);
    }

}
