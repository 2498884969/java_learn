package com.qxh.autoconfig.config;

import com.qxh.autoconfig.annotation.EnableHelloWorld;
import com.qxh.condition.ConditionalOnSystemProperty;
import org.springframework.context.annotation.Configuration;

@Configuration                                                          // spring模式注解
@EnableHelloWorld                                                       // 模块装配
@ConditionalOnSystemProperty(name = "user.name", value = "qiangxuhui")
public class HelloWorldAutoConfiguration {
}
