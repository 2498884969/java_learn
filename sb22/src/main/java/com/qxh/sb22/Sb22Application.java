package com.qxh.sb22;

import com.qxh.sb22.initializer.SecondInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Sb22Application {

    public static void main(String[] args) {

//        SpringApplication app = new SpringApplication(Sb22Application.class);
//
//        app.addInitializers(new SecondInitializer());
//
//        app.run(args);
        SpringApplication.run(Sb22Application.class, args);
    }

}
