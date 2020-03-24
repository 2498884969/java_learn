package com.qxh.adapter;

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

}
