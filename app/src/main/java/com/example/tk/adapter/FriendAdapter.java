package com.example.tk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tk.dao.FriendInfo;
import com.example.tk.R;

import java.util.List;

public class FriendAdapter extends ArrayAdapter<FriendInfo> {
    private Context context;
    private List<FriendInfo> friends;

    public FriendAdapter(Context context, List<FriendInfo> friends) {
        super(context, 0, friends);
        this.context = context;
        this.friends = friends;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_friend, parent, false);
        }

        TextView friendName = convertView.findViewById(R.id.tvFriendName);
        FriendInfo friendInfo = friends.get(position);
        friendName.setText(friendInfo.getFriendname());

        return convertView;
    }
}