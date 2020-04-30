package com.imooc.rabbit;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class RabbitSender {

    @Autowired
    RabbitTemplate rabbitTemplate;

    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback(){

        /**
         *
         * @param correlationData   消息的唯一标识
         * @param ack               消息是否发送到broker
         * @param cause             消息发送失败的原因
         */
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.err.println("消息发送成功");
            System.err.println(correlationData);
            System.err.println(ack);
            System.err.println(cause);
        }
    };

    /**
     *String exchange, String routingKey, Object message, MessagePostProcessor messagePostProcessor,
     * 			CorrelationData correlationData
     *
     * @param message
     * @param properties
     */
    public void send(Object message, Map<String, Object> properties) {

        MessageHeaders mhs = new MessageHeaders(properties);
        Message msg = MessageBuilder.createMessage(message, mhs);

        rabbitTemplate.setConfirmCallback(confirmCallback);
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());

        rabbitTemplate.convertAndSend("exchange-1", "springboot.hello", msg, message1 -> {
            System.err.println("消息已经发送");
            return message1;
        },correlationData);

    }

}
