package com.qxh.autoconfig.annotation;

import com.qxh.autoconfig.config.HelloWorldConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
//@Import({HelloWorldConfiguration.class})
@Import(HelloWorldImportSelector.class)
public @interface EnableHelloWorld {
// 1. 基于注解的enable
// 2. @Import 可以使得类实例化, 并且在没有configration的情况下同样可以使得bean进行实例化
// 3. select 更有弹性
}
