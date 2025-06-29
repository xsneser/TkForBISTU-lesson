package com.example.tk.backActivity;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.example.tk.testweb;

public class receiveMessageService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        Log.i("reciveMessageService", "oncreate");
        String username = sharedPreferences.getString("username", "未登录");
        String ID = sharedPreferences.getString("ID", "点击登录");
        Log.i("reciveMessageService", username + "  " + ID);
        // 初始化操作，如注册广播接收器、启动线程等
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        Log.i("reciveMessageService", "onStartCommand");
        String username = sharedPreferences.getString("username", "未登录");
        String ID = sharedPreferences.getString("ID", "点击登录");
        Log.i("reciveMessageService", username + "  " + ID);

        testweb inmessage = new testweb();
        inmessage.toserve("R" + ID);
        String message = inmessage.outmessage;
        Log.i("reciveMessageService", "消息：" + message);

        // 处理接收到的消息
        handleReceivedMessage(message);

        return Service.START_REDELIVER_INTENT;
    }

    private void handleReceivedMessage(String message) {
        // 在这里处理接收到的消息
        // 例如，更新数据库或通知 `SecondFragment` 更新消息列表
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}