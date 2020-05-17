package com.qxh.sb22.ioc.ann;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class MyBeanFactoryPostprocessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition teacherBean = beanFactory.getBeanDefinition("teacher");
        MutablePropertyValues mutablePropertyValues = teacherBean.getPropertyValues();
        mutablePropertyValues.addPropertyValue("name", "wangwu");
    }
}
