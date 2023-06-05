package com.banking;

import com.banking.service.connection.SocketConnectionServer;

public class Main {
    public static void main(String[] args) {
        new SocketConnectionServer().start();
    }
}
