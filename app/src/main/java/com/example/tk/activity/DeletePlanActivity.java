package com.example.tk.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tk.R;
import com.example.tk.userDatabase.user_database;

import java.util.ArrayList;
import java.util.List;

public class DeletePlanActivity extends AppCompatActivity {
    private static final String TAG = "DeletePlanActivity";

    private user_database userDb;
    private TextView tvDate;
    private RecyclerView recyclerViewPlans;
    private String selectedDate;
    private List<Plan> planList;
    private PlanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_plan);

        userDb = new user_database(this);
        planList = new ArrayList<>();

        // 获取传递的日期
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("date")) {
            selectedDate = intent.getStringExtra("date");
        } else {
            Toast.makeText(this, "日期获取失败", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 初始化日期显示TextView
        tvDate = findViewById(R.id.tv_delete_date);
        tvDate.setText(selectedDate);

        // 初始化RecyclerView
        recyclerViewPlans = findViewById(R.id.rv_plan_list);
        recyclerViewPlans.setLayoutManager(new LinearLayoutManager(this));

        // 修改：传递context和planList到适配器构造函数
        adapter = new PlanAdapter(this, planList);
        recyclerViewPlans.setAdapter(adapter);

        // 设置删除按钮点击事件
        adapter.setOnDeleteClickListener((position, planId) -> deletePlan(position, planId));

        // 加载计划列表
        loadPlanList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPlanList();
    }

    private void loadPlanList() {
        try {
            planList.clear();
            planList.addAll(userDb.queryPlansByDate(selectedDate));
            adapter.notifyDataSetChanged();

            if (planList.isEmpty()) {
                Toast.makeText(this, "今日暂无计划", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "加载计划列表失败: " + e.getMessage());
            Toast.makeText(this, "加载计划失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void deletePlan(int position, int planId) {
        try {
            userDb.deletePlan(planId);
            Toast.makeText(this, "计划已删除", Toast.LENGTH_SHORT).show();
            loadPlanList(); // 刷新列表

            // 返回结果给UserMainActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("date", selectedDate);
            setResult(RESULT_OK, resultIntent);
        } catch (Exception e) {
            Log.e(TAG, "删除计划失败: " + e.getMessage());
            Toast.makeText(this, "删除失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}