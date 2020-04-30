package com.imooc.test;


import com.imooc.rabbit.RabbitSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitTest {

    @Autowired
    RabbitSender rabbitSender;


    @Test
    public void testSend(){

        Map<String, Object> headers = new HashMap<>();
        headers.put("attr1", "123456");

        rabbitSender.send("hello rabbitmq haha", headers);

    }

}
