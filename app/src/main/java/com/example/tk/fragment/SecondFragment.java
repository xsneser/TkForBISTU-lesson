package com.example.tk.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tk.adapter.MessageAdapter;
import com.example.tk.dao.MessageInfo;
import com.example.tk.databinding.FragmentSecondBinding;

import com.example.tk.R;
import com.example.tk.userDatabase.user_database;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {

    private TextView tvFriendName;
    private EditText etMessage;
    private Button buttonSend;
    private RecyclerView rvMessages;
    private MessageAdapter messageAdapter;
    private List<MessageInfo> messagesList;
    private user_database dbHelper;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etMessage = view.findViewById(R.id.etMessage);
        buttonSend = view.findViewById(R.id.button_send);
        rvMessages = view.findViewById(R.id.rvMessages);
        tvFriendName = view.findViewById(R.id.tvFriendName);

        dbHelper = new user_database(getContext());
        messagesList = new ArrayList<>();
        messageAdapter = new MessageAdapter(getContext(), messagesList);
        rvMessages.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            String friendName = getArguments().getString("friend_name");
            tvFriendName.setText("You are now chatting with: " + friendName);
        }
        rvMessages.setAdapter(messageAdapter);

        buttonSend.setOnClickListener(v -> {
            String messageContent = etMessage.getText().toString();
            if (!messageContent.isEmpty()) {
                // 假设发送者是当前用户，接收者是朋友
                String sender = "current_user"; // 替换为当前用户逻辑
                String receiver = tvFriendName.getText().toString().replace("You are now chatting with: ", "");
                long timestamp = System.currentTimeMillis();

                // 插入消息到数据库
                dbHelper.insertMessage(sender, receiver, messageContent, String.valueOf(timestamp));

                // 添加到消息列表并刷新适配器
                messagesList.add(new MessageInfo(sender, receiver, messageContent, String.valueOf(timestamp)));
                messageAdapter.notifyDataSetChanged();

                // 清空输入框
                etMessage.setText("");
            }
        });

        // 查询消息记录
        loadMessages();
    }

    private void loadMessages() {
        // 假设当前用户是 "current_user"，朋友是 tvFriendName 的文本
        String friendName = tvFriendName.getText().toString().replace("You are now chatting with: ", "");
        messagesList.clear();
        messagesList.addAll(dbHelper.queryMessages("current_user", friendName));
        messageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}