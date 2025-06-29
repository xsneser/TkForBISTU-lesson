package com.example.tk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class testweb {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String serverIp = "10.153.129.199"; // 模拟器中访问本地主机的 IP
    private int serverPort = 29898;
    private boolean isConnected = false;
    private CountDownLatch connectionLatch = new CountDownLatch(1);

    public String outmessage;

    public void toserve(String message) {
        // 确保只连接一次
        if (!isConnected) {
            new Thread(this::connectToServer).start();
        }

        try {
            // 等待连接建立
            connectionLatch.await();

            if (!message.isEmpty() && isConnected) {
                new Thread(() -> sendMessage(message)).start();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        //disconnect();
    }

    private void connectToServer() {
        try {
            socket = new Socket(serverIp, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            isConnected = true;
            // 连接已建立，释放等待的线程
            connectionLatch.countDown();
            // 开始接收消息
            receiveMessages();
        } catch (IOException e) {
            e.printStackTrace();
            // 连接失败，也释放等待的线程
            connectionLatch.countDown();
        }
    }

    private void receiveMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                final String finalMessage = message;
                System.out.println("收到消息: " + finalMessage);
                outmessage = finalMessage;
                if ("再见".equalsIgnoreCase(finalMessage)) {
                    disconnect();
                    break;
                }
            }
        } catch (IOException e) {
            if (isConnected) {
                disconnect();
            }
        }
    }

    private void sendMessage(String message) {
        if (out != null) {
            out.println(message);
            System.out.println("发送消息: " + message);
            disconnect();
        }
    }

    public void disconnect() {
        try {
            isConnected = false;
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("已断开连接");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}