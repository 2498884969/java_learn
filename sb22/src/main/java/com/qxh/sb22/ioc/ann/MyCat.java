package com.qxh.sb22.ioc.ann;

import com.qxh.sb22.ioc.Animal;
import com.qxh.sb22.ioc.Cat;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * 使用工厂bean
 */
@Component
public class MyCat implements FactoryBean<Animal> {
    @Override
    public Animal getObject() throws Exception {
        return new Cat();
    }

    @Override
    public Class<?> getObjectType() {
        return Animal.class;
    }
}
