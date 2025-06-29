package com.example.tk.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tk.R;

public class DetailFragment extends Fragment {
    private TextView timeAxisText;
    private TextView planContentText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        timeAxisText = view.findViewById(R.id.time_axis);
        planContentText = view.findViewById(R.id.plan_content);
        return view;
    }

    // 更新详情显示的方法
    public void updateDateDetail(String date, String plan) {
        if (timeAxisText != null) {
            timeAxisText.setText(date);
        }
        if (planContentText != null) {
            planContentText.setText(plan);
        }
    }
}