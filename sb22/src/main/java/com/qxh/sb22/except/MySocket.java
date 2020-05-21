package com.qxh.sb22.except;

import java.net.ServerSocket;

public class MySocket {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8080);
        serverSocket.accept();
    }

}
