package com.bfxy.base.rabbit;

import com.bfxy.base.rabbit.exception.MessageRunTimeException;

import java.util.List;

public interface MessageProducer {

    void send(Message message, SendCallback callback) throws MessageRunTimeException;

    void send(Message message) throws MessageRunTimeException;

    void send(List<Message> messages) throws MessageRunTimeException;

}
