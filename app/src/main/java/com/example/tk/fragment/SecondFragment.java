package com.example.tk.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.tk.dao.MessageInfo;
import com.example.tk.databinding.FragmentSecondBinding;
import com.example.tk.R;
import com.example.tk.testweb;
import com.example.tk.userDatabase.user_database;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private EditText etMessage;
    private Button buttonSend;
    private RecyclerView rvMessages;
    private user_database dbHelper;
    private List<MessageInfo> messagesList;
    private MessageAdapter messageAdapter;

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
                long timestamp = System.currentTimeMillis();
                // 插入消息到数据库
                dbHelper.insertMessage(userid, fid, messageContent, String.valueOf(timestamp));
                // 添加到消息列表并刷新适配器
                messagesList.add(new MessageInfo(userid, fid, messageContent, String.valueOf(timestamp)));
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
        binding = null;
    }
}