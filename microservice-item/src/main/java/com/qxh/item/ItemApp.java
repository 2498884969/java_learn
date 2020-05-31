package com.qxh.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ItemApp {

    public static void main(String[] args) {
        SpringApplication.run(ItemApp.class, args);
    }

}
