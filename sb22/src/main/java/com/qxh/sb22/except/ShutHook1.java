package com.qxh.sb22.except;

public class ShutHook1 {

    public static void main(String[] args) {
        System.err.println("hello");
        Thread close_jvm = new Thread(() -> System.out.println("close jvm"));
        Runtime.getRuntime().addShutdownHook(close_jvm);
        System.err.println("world");
//        Runtime.getRuntime().removeShutdownHook(close_jvm);
    }

}
