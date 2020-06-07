package com.qxh.autoconfig.bootstrap;

import com.qxh.autoconfig.service.CalculateService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "com.qxh.autoconfig.service")
public class CalculateServiceBootstrap {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = new SpringApplicationBuilder(CalculateServiceBootstrap.class)
                .web(WebApplicationType.NONE)
                .profiles("Java7")
                .run(args);

        CalculateService calculateService = context.getBean(CalculateService.class);

        System.out.println(
                "calculateService.sum(1...10) : " +
                        calculateService.sum(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        );

        context.close();
    }

}
