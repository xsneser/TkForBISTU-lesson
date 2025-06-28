package com.example.tk;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class testweb extends AppCompatActivity {
    private EditText messageView;
    private EditText messageInput;
    private Button sendButton;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Handler handler;
    private String serverIp = "10.153.115.69"; // 模拟器中访问本地主机的 IP
    private int serverPort = 29898;
    private boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webtest_layout);

        messageView = findViewById(R.id.messageView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        handler = new Handler(Looper.getMainLooper());


        // 设置 messageView 为不可编辑
        messageView.setKeyListener(null);
        messageView.setFocusable(false);
        messageView.setFocusableInTouchMode(false);
        messageView.setEnabled(false);

        // 初始化连接
        new Thread(this::connectToServer).start();

        // 发送消息按钮点击事件
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageInput.getText().toString().trim();
                if (!message.isEmpty() && isConnected) {
                    new Thread(() -> sendMessage(message)).start();
                    messageInput.setText("");
                }
            }
        });
    }

    private void connectToServer() {
        try {
            socket = new Socket(serverIp, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            isConnected = true;

            updateUI("已连接到服务器");

            // 开始接收消息
            receiveMessages();
        } catch (IOException e) {
            updateUI("连接失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void receiveMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                final String finalMessage = message;
                updateUI("服务器: " + finalMessage);

                if ("再见".equalsIgnoreCase(finalMessage)) {
                    disconnect();
                    break;
                }
            }
        } catch (IOException e) {
            if (isConnected) {
                updateUI("接收消息失败: " + e.getMessage());
                disconnect();
            }
        }
    }

    private void sendMessage(String message) {
        if (out != null) {
            out.println(message);
            updateUI("我: " + message);
        }
    }

    private void disconnect() {
        try {
            isConnected = false;
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            updateUI("已断开连接");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateUI(String message) {
        handler.post(() -> {
            messageView.append(message + "\n");
            // 滚动到底部
            messageView.setSelection(messageView.getText().length());
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnect();
    }
}
