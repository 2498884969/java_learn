package com.qxh.springcloud.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaApp9100 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApp9100.class, args);
    }

}
