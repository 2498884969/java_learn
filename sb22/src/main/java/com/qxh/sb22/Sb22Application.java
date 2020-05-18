package com.qxh.sb22;

import com.qxh.sb22.initializer.SecondInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StopWatch;

@SpringBootApplication
public class Sb22Application {

    public static void main(String[] args) throws Exception {

//        SpringApplication app = new SpringApplication(Sb22Application.class);
//
//        app.addInitializers(new SecondInitializer());
//
//        app.run(args);
        SpringApplication.run(Sb22Application.class, args);

//        StopWatch myWatch = new StopWatch("myWatch");
//        myWatch.start("task1");
//        Thread.sleep(2000);
//        myWatch.stop();
//        myWatch.start("task2");
//        Thread.sleep(3000);
//        myWatch.stop();
//        myWatch.start("task3");
//        Thread.sleep(1000);
//        myWatch.stop();
//        System.out.println(myWatch.prettyPrint());

    }

}
