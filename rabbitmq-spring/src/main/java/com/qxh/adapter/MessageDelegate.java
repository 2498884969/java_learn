package com.qxh.adapter;

import com.qxh.entity.Order;
import com.qxh.entity.Packaged;

import java.io.File;
import java.util.Map;

public class MessageDelegate {

//    /**
//     * 默认使用
//     */
    public void handleMessage(String messageBody) {
        System.err.println("默认方法, 消息内容, string:" + messageBody);
    }

    public void handleMessage(byte[] messageBody) {
        System.err.println("默认方法, 消息内容, bytes:" + new String(messageBody));
    }

    public void consumeMessage(byte[] messageBody) {
        System.err.println("字节数组方法, 消息内容:" + new String(messageBody));
    }

    /**
     * 需要添加消息转换器
     * @param messageBody
     */
    public void consumeMessage(String messageBody) {
        System.err.println("字符串方法, 消息内容:" + messageBody);
    }

    public void method1(String messageBody) {
        System.err.println("method1 收到消息内容:" + messageBody);
    }

    public void method2(String messageBody) {
        System.err.println("method2 收到消息内容:" + messageBody);
    }

    /**
     * Jackson2JsonMessageConverter
     * @param messageBody
     */
    public void consumeMessage(Map messageBody) {
        System.err.println("map方法, 消息内容:" + messageBody);
    }

    /**
     * Jackson2JsonMessageConverter
     * DefaultJackson2JavaTypeMapper
     * @param order
     */
    public void consumeMessage(Order order) {
        System.err.println("order对象, 消息内容, id: " + order.getId() +
                ", name: " + order.getName() +
                ", content: "+ order.getContent());
    }

    /**
     * 打包
     * @param pack
     */
    public void consumeMessage(Packaged pack) {
        System.err.println("package对象, 消息内容, id: " + pack.getId() +
                ", name: " + pack.getName() +
                ", content: "+ pack.getDescription());
    }

    public void consumeMessage(File file) {
        System.err.println("文件对象 方法, 消息内容:" + file.getName());
    }

}
