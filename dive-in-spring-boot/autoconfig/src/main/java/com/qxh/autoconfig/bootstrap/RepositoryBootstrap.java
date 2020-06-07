package com.qxh.autoconfig.bootstrap;

import com.qxh.autoconfig.repository.MyFirstRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.qxh.autoconfig.repository"})
public class RepositoryBootstrap {

    public static void main(String[] args) {

        // 1. RepositoryBootstrap 指定了扫描配置类
        ConfigurableApplicationContext context = new SpringApplicationBuilder(RepositoryBootstrap.class)
                .web(WebApplicationType.NONE)
                .run();

        System.out.println(
                context.getBean(MyFirstRepository.class)
        );

        context.close();
    }

}
