package com.imooc.rabbit;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MRabbitListener {

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "queue-1", durable = "true"),
                    exchange = @Exchange(name = "exchange-1", type = "topic"),
                    key = "springboot.*"
            )
    )
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws IOException {
//     选取何种处理消息的方法取决于 @payload

        // 1. 消费消息
        System.err.println("消费消息：" + message.getPayload());
        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        // 2. 进行消息确认
        channel.basicAck(deliveryTag, false);
    }

}
