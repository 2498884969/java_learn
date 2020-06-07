package com.qxh.autoconfig.annotation;

import org.springframework.stereotype.Repository;

import java.lang.annotation.*;

@Target({ElementType.TYPE})                 //  1.作用域在类上
@Retention(RetentionPolicy.RUNTIME)         //  2.运行时注解
@Documented
@Repository                                 //  3. 注解合并
public @interface FirstLevelRepository {
    String value() default "";
}
