package com.banking.service.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketConnectionServer {
    private static final int SERVER_PORT = 11111;
    private final RequestHandler requestHandler;

    public SocketConnectionServer() {
        requestHandler = new RequestHandler();
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            System.out.printf("Server started and listening on port %d...\n", SERVER_PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected.");

                // Receive the SocketRequest object from the client
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                SocketRequest request = (SocketRequest) objectInputStream.readObject();

                System.out.println("Received SocketRequest from the client:");
                System.out.println("Account Number: " + request.getAccountNumber());
                System.out.println("Request Type: " + request.getRequestType());
                System.out.println("Body: " + request.getBody());

                // Process the request and create a SocketResponse object
                SocketResponse response = requestHandler.handleRequest(request);

                // Send the SocketResponse object to the client
                objectOutputStream.writeObject(response);
                objectOutputStream.flush();
                System.out.println("SocketResponse sent to the client.");

                // Close the resources
                objectOutputStream.close();
                objectInputStream.close();
                socket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}