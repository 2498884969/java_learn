package com.qxh.sb22.ioc.ann;

import com.qxh.sb22.ioc.Dog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfigration {

    @Bean
    public Dog getDog(){
        return new Dog();
    }

}
