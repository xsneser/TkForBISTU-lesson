package com.example.tk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tk.SignLog.LoginActivity;
import com.example.tk.R;
import android.content.SharedPreferences;
import android.widget.TextView;

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
                Intent intent = new Intent(UserMainMeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void onRestart() {
        refresh();
        super.onRestart();
    }

    private void refresh(){
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false);

        if (isLoggedIn) {
            // 获取用户信息
            String username = sharedPreferences.getString("username", "未登录");
            String ID = sharedPreferences.getString("ID","点击登录");
            // 更新界面显示
            TextView tvUsername = findViewById(R.id.userName);
            TextView tvID=findViewById(R.id.userID);

            tvUsername.setText(username);
            tvID.setText("UID:"+ID);

        }

    }

}
