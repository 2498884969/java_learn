package com.bfxy.rabbit.task.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.bfxy.rabbit.task.autoconfigure.JobParserAutoConfigurartion;
import org.springframework.scheduling.annotation.EnableScheduling;

//@Import https://www.cnblogs.com/yichunguo/p/12122598.html
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(JobParserAutoConfigurartion.class)
public @interface EnableElasticJob {

}
