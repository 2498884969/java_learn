package com.qxh.sb22;

import com.qxh.sb22.condi.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@SpringBootTest
public class Sb22Condi implements ApplicationContextAware {


    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    @Test
    public void conditionalOnProperty(){
        System.out.println(
                applicationContext.getBean(A.class)
        );
    }
}
