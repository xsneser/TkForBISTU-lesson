package com.example.tk.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tk.fragment.CalendarFragment;
import com.example.tk.fragment.DetailFragment;
import com.example.tk.R;
import androidx.annotation.Nullable;
import com.example.tk.backActivity.Insertuser_Activity;
import com.example.tk.SignLog.Sea_deluser_Activity;
import com.example.tk.userDatabase.user_database;
import com.example.tk.activity.Plan;

import java.util.List;

public class UserMainActivity extends AppCompatActivity implements CalendarFragment.OnDateSelectedListener {
    public user_database user;
    private DetailFragment detailFragment;
    private Button addPlanButton;
    private Button deletePlanButton;
    private String currentDate; // 记录当前选中的日期
    private static final int ADD_PLAN_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        user = new user_database(this);
        detailFragment = (DetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detailFragment);

        // 添加和删除计划的按钮
        addPlanButton = findViewById(R.id.add_plan_button);
        deletePlanButton = findViewById(R.id.delete_plan_button);

        addPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentDate != null) {
                    // 跳转到添加计划页面，并传递当前日期
                    Intent intent = new Intent(UserMainActivity.this, AddPlanActivity.class);
                    intent.putExtra("date", currentDate);
                    startActivityForResult(intent, ADD_PLAN_REQUEST_CODE);
                } else {
                    Toast.makeText(UserMainActivity.this, "请先选择日期", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deletePlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentDate != null) {
                    // 跳转到删除计划页面，并传递当前日期
                    Intent intent = new Intent(UserMainActivity.this, DeletePlanActivity.class);
                    intent.putExtra("date", currentDate);
                    startActivity(intent);
                } else {
                    Toast.makeText(UserMainActivity.this, "请先选择日期", Toast.LENGTH_SHORT).show();
                }
            }
        });
        LinearLayout messageView = findViewById(R.id.linearLayout2);
        messageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMainActivity.this, UserMainMessageActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        LinearLayout newView = findViewById(R.id.linearLayout3);
        newView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMainActivity.this, UserMainNewActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        LinearLayout meView = findViewById(R.id.linearLayout4);
        meView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMainActivity.this, UserMainMeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });


        // 日历选择监听器
        CalendarFragment calendarFragment = (CalendarFragment) getSupportFragmentManager()
                .findFragmentById(R.id.calendarFragment);
        calendarFragment.setOnDateSelectedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PLAN_REQUEST_CODE && resultCode == RESULT_OK) {
            // 获取返回的日期
            if (data != null && data.hasExtra("date")) {
                currentDate = data.getStringExtra("date");
                // 重新加载当天的计划
                String plan = getPlanFromDatabase(currentDate);
                Log.d("UserMainActivity", "重新加载的计划内容: " + plan);

                if (detailFragment != null) {
                    detailFragment.updateDateDetail(currentDate, plan);
                }
            }
        }
    }

    public void onDateSelected(int year, int month, int dayOfMonth) {
        // 处理日期（月份从 0 开始，所以 +1）
        currentDate = year + "-" + (month + 1) + "-" + dayOfMonth;

        // 从数据库中查询当日计划
        String plan = getPlanFromDatabase(currentDate);

        // 更新 DetailFragment 的显示
        if (detailFragment != null) {
            detailFragment.updateDateDetail(currentDate, plan);
        }
    }

    // 从数据库中查询当日计划
    private String getPlanFromDatabase(String date) {
        StringBuilder planBuilder = new StringBuilder();
        List<Plan> plans = user.queryPlansByDate(date);

        for (Plan plan : plans) {
            planBuilder.append(plan.getTime()).append(" - ").append(plan.getContent()).append("\n\n");
        }

        if (planBuilder.length() == 0) {
            planBuilder.append("暂无计划");
        }

        return planBuilder.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (user != null) {
            user.close();
        }
    }
}