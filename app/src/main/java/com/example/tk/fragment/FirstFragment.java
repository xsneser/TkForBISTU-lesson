package com.example.tk.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.tk.adapter.FriendAdapter;
import com.example.tk.dao.FriendInfo;
import com.example.tk.databinding.FragmentFirstBinding;

import com.example.tk.R;
import com.example.tk.userDatabase.user_database;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

     private FragmentFirstBinding binding;
    private ListView lvFriends;
    private user_database dbHelper;
    private List<FriendInfo> friendsList;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lvFriends = view.findViewById(R.id.lv);
        dbHelper = new user_database(getContext());
        friendsList = new ArrayList<>();

        binding.buttonFirst.setOnClickListener(v ->
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_firstFragment_to_secondFragment)
        );

        List<FriendInfo> friendInfos = dbHelper.query_f(null);
        friendsList.addAll(friendInfos);

        // 设置适配器
        FriendAdapter friendAdapter = new FriendAdapter(getContext(), friendsList);
        lvFriends.setAdapter(friendAdapter);

        // 设置点击事件
        lvFriends.setOnItemClickListener((parent, view1, position, id) -> {
            FriendInfo selectedFriend = friendsList.get(position);
            Bundle bundle = new Bundle();
            bundle.putString("friend_name", selectedFriend.getFriendname());
            NavHostFragment.findNavController(FirstFragment.this)
                    .navigate(R.id.action_firstFragment_to_secondFragment, bundle);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}