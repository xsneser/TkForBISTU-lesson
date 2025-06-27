package com.example.tk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tk.R;
import com.example.tk.SignLog.LoginActivity;
import com.example.tk.SignLog.Sea_deluser_Activity;
import com.example.tk.SignLog.choiseUser_Activity;

import android.content.SharedPreferences;
import android.widget.TextView;
import android.widget.Toast;
public class UserMainMeActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_me_layout);

        refresh();

        LinearLayout messageView = findViewById(R.id.linearLayout2);
        messageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMainMeActivity.this, UserMainMessageActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        LinearLayout newView = findViewById(R.id.linearLayout3);
        newView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMainMeActivity.this, UserMainNewActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        LinearLayout meView = findViewById(R.id.linearLayout1);
        meView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMainMeActivity.this, UserMainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        LinearLayout loginView = findViewById(R.id.login);

        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMainMeActivity.this, choiseUser_Activity.class);
                startActivity(intent);
            }
        });

        // 退出登录功能
        LinearLayout logoutView = findViewById(R.id.logoutItem);
        logoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 检查登录状态
                SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                boolean isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false);

                if (isLoggedIn) {
                    // 只有已登录时才执行退出操作
                    new AlertDialog.Builder(UserMainMeActivity.this)
                            .setTitle("退出登录")
                            .setMessage("确定要退出当前账号吗？")
                            .setPositiveButton("确定", (dialog, which) -> {
                                // 清除登录状态
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("is_logged_in", false);
                                editor.remove("username");
                                editor.remove("ID");
                                editor.apply();
                                // 刷新界面UI
                                refresh();

                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext() ,"您尚未登陆",Toast.LENGTH_SHORT);
                    toast.show();

                }

                }

        });
    }


    protected void onRestart() {
        refresh();
        super.onRestart();
    }

    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh(){
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false);


            // 获取用户信息
            String username = sharedPreferences.getString("username", "未登录");
            String ID = sharedPreferences.getString("ID","点击登录");
            // 更新界面显示
            TextView tvUsername = findViewById(R.id.userName);
            TextView tvID=findViewById(R.id.userID);

            tvUsername.setText(username);
        if (isLoggedIn) {
            tvID.setText("UID:"+ID);
        }else{
            tvID.setText(ID);
        }


    }

}
