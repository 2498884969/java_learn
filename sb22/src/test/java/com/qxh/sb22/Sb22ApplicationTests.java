package com.qxh.sb22;

import com.qxh.sb22.ioc.HelloService;
import com.qxh.sb22.ioc.ann.MyBeanImport;
import com.qxh.sb22.ioc.ann.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
//@ContextConfiguration(locations = {"classpath:ioc/demo.xml"})
//@Import({MyBeanImport.class})
class Sb22ApplicationTests {

    @Autowired
    private HelloService helloService;

    @Autowired
    Teacher teacher;

    @Test
    public void ioc1(){
        System.err.println(
            helloService.hello()
        );
        // ------------------------------------------------------------
        System.err.println(
                helloService.hello2()
        );
    }

    @Test
    public void ioc2() {
        System.err.println(teacher.getName());
    }

//    @Autowired
//    WeatherRunListener weatherRunListener;

//    @Test
//    void listenerTest(){
//        weatherRunListener.rain();
//        weatherRunListener.snow();
//    }

    @Test
    void contextLoads() {
    }



}
