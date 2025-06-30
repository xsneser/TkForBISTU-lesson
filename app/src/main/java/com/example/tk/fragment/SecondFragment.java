package com.example.tk.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tk.adapter.MessageAdapter;
import com.example.tk.backActivity.MessageAction;
import com.example.tk.dao.MessageInfo;
import com.example.tk.databinding.FragmentSecondBinding;
import com.example.tk.testweb;
import com.example.tk.userDatabase.user_database;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private EditText etMessage;
    private Button buttonSend;
    private RecyclerView rvMessages;
    private user_database dbHelper;
    private List<MessageInfo> messagesList;
    private MessageAdapter messageAdapter;
    private MessageAction ma;
    private ScheduledExecutorService executor;
    private Handler handler;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etMessage = binding.etMessage;
        buttonSend = binding.buttonSend;
        rvMessages = binding.rvMessages;
        ma = new MessageAction();
        dbHelper = new user_database(requireContext());
        messagesList = new ArrayList<>();
        messageAdapter = new MessageAdapter(requireContext(), messagesList);
        rvMessages.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvMessages.setAdapter(messageAdapter);

        // 获取传递过来的参数
        Bundle args = getArguments();
        String fid;
        if (args != null) {
            fid = args.getString("friend_name", "");
        } else {
            fid = "";
        }

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_info", MODE_PRIVATE);
        String userid = sharedPreferences.getString("ID", "");

        buttonSend.setOnClickListener(v -> {
            String messageContent = etMessage.getText().toString();
            if (!messageContent.isEmpty()) {
                LocalDateTime currentTime = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    currentTime = LocalDateTime.now();
                }
                DateTimeFormatter formatter = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                }
                String timestamp = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    timestamp = currentTime.format(formatter);
                }
                // 插入消息到数据库
                dbHelper.insertMessage(userid, fid, messageContent, "["+timestamp+"]");
                // 添加到消息列表并刷新适配器
                messagesList.add(new MessageInfo(userid, fid, messageContent, "["+timestamp+"]"));
                messageAdapter.notifyDataSetChanged();
                testweb inmessage = new testweb();
                inmessage.toserve("M" + fid + "|" + messageContent + "|" + userid);
                String IsLOGIN = inmessage.outmessage;
                // 清空输入框
                etMessage.setText("");
            }
        });

        // 查询消息记录
        loadMessages(fid, userid);

        // 每秒执行一次接收消息的任务
        executor = Executors.newSingleThreadScheduledExecutor();
        handler = new Handler(Looper.getMainLooper());
        executor.scheduleWithFixedDelay(() -> {
            testweb imessage = new testweb();
            imessage.toserve("R" + userid);
            String getmg = imessage.outmessage;
            Log.i("SecondFragment", getmg);

            if (getmg != null && !getmg.isEmpty()) {
                // 处理接收到的消息
                String time = ma.getFirstLeftPipe(getmg);
                String content = ma.getCenter(getmg);
                String frid = ma.getContentAfterRightPipe(getmg);
                Log.i("SecondFragment", time + " " + content + " " + frid);

                // 插入消息到数据库
                dbHelper.insertMessage(frid, userid, content, time);

                // 在主线程中更新 UI
                handler.post(() -> {
                    messagesList.add(new MessageInfo(frid, userid, content, time));
                    messageAdapter.notifyDataSetChanged();
                    loadMessages(userid, fid);
                });
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void onPause() {
        // 获取传递过来的参数
        Bundle args = getArguments();
        String fid;
        if (args != null) {
            fid = args.getString("friend_name", "");
        } else {
            fid = "";
        }
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_info", MODE_PRIVATE);
        String userid = sharedPreferences.getString("ID", "");
        // 每秒执行一次接收消息的任务
        executor = Executors.newSingleThreadScheduledExecutor();
        handler = new Handler(Looper.getMainLooper());
        executor.scheduleWithFixedDelay(() -> {
            testweb imessage = new testweb();
            imessage.toserve("R" + userid);
            String getmg = imessage.outmessage;
            Log.i("SecondFragment", getmg);

            if (getmg != null && !getmg.isEmpty()) {
                // 处理接收到的消息
                String time = ma.getFirstLeftPipe(getmg);
                String content = ma.getCenter(getmg);
                String frid = ma.getContentAfterRightPipe(getmg);
                Log.i("SecondFragment", time + " " + content + " " + frid);

                // 插入消息到数据库
                dbHelper.insertMessage(frid, userid, content, time);

                // 在主线程中更新 UI
                handler.post(() -> {
                    messagesList.add(new MessageInfo(frid, userid, content, time));
                    messageAdapter.notifyDataSetChanged();
                    loadMessages(userid, fid);
                });
            }
        }, 0, 1, TimeUnit.SECONDS);
        super.onPause();
    }

    private void loadMessages(String fid, String userid) {
        // 假设当前用户是 "current_user"，朋友是 "friend_user"
        messagesList.clear();
        messagesList.addAll(dbHelper.queryMessages(userid, fid));
        messageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dbHelper != null) {
            dbHelper.close();
        }
        if (executor != null) {
            executor.shutdownNow(); // 停止任务
        }
        binding = null;
    }
}