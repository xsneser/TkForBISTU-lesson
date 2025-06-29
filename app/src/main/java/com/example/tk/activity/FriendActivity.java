package com.example.tk.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tk.R;
import com.example.tk.userDatabase.user_database;

import java.util.Calendar;

public class FriendActivity extends AppCompatActivity {
    private static final String TAG = "FriendActivity";

    private EditText etPlanContent;
    private TextView tvSelectedDate, tvSelectedTime;
    private Button btnSendPlan;
    private user_database userDb;
    private String selectedDate;
    private String selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_plan);

        userDb = new user_database(this);

        etPlanContent = findViewById(R.id.et_plan_content);
        tvSelectedDate = findViewById(R.id.tv_selected_date);
        tvSelectedTime = findViewById(R.id.tv_selected_time);
        btnSendPlan = findViewById(R.id.btn_send_plan);

        // 获取从其他Activity传递的日期
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("date")) {
            selectedDate = intent.getStringExtra("date");
            tvSelectedDate.setText(selectedDate);
        } else {
            // 如果没有传递日期，默认使用今天
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            selectedDate = year + "-" + (month + 1) + "-" + day;
            tvSelectedDate.setText(selectedDate);
        }

        // 设置日期选择器
        tvSelectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // 设置时间选择器
        tvSelectedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        // 发送计划按钮
        btnSendPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePlanToDatabase();
            }
        });
    }

    private void showDatePickerDialog() {
        String[] dateParts = selectedDate.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]) - 1;
        int day = Integer.parseInt(dateParts[2]);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                        tvSelectedDate.setText(selectedDate);
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                        tvSelectedTime.setText(selectedTime);
                    }
                },
                hour, minute, true);
        timePickerDialog.show();
    }

    private void savePlanToDatabase() {
        String content = etPlanContent.getText().toString().trim();

        if (content.isEmpty()) {
            Toast.makeText(this, "计划内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedTime == null || selectedTime.isEmpty()) {
            Toast.makeText(this, "请选择时间", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // 获取可写数据库
            SQLiteDatabase db = userDb.getWritableDatabase();

            // 调用修改后的 addPlan 方法（传入已获取的数据库实例）
            userDb.addPlan(db, selectedDate, selectedTime, content);

            Log.d(TAG, "好友计划添加成功");
            Toast.makeText(this, "好友计划添加成功", Toast.LENGTH_SHORT).show();

            // 返回数据给调用的Activity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("date", selectedDate);
            setResult(RESULT_OK, resultIntent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "好友计划添加异常: " + e.getMessage());
            Toast.makeText(this, "好友计划添加异常: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userDb != null) {
            userDb.close();
        }
    }
}