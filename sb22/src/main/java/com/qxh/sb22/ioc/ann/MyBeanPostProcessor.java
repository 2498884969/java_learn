package com.qxh.sb22.ioc.ann;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

//@Component
public class MyBeanPostProcessor implements InstantiationAwareBeanPostProcessor {

    // 1. 在bean实例化阶段进行处理

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (beanName.equals("worker")) {
            System.err.println(beanClass.getName());
//            return new Worker();
        }
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("worker")) {
            Worker worker = (Worker) bean;
            worker.setName("wangwu");
        }
        return bean;
    }
}
