package com.example.tk.backActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tk.R;
import com.example.tk.userDatabase.user_database;

public class starActivity extends Activity {
    public user_database user;
    public SQLiteDatabase sql_read;
    private Button search_del_btn, insert_btn, updata_bin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        user = new user_database(this);
        sql_read = user.getReadableDatabase();
        init();
    }

    public void init() {
        // 初始化按钮
        search_del_btn = findViewById(R.id.search_delete);
        insert_btn = findViewById(R.id.add);
        updata_bin = findViewById(R.id.update);

        // 为“查询/删除”按钮设置点击监听器
        search_del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(starActivity.this, Sea_deluser_Activity.class);
                    startActivity(intent);
                    // 可添加过渡动画（如需要）
                    // overridePendingTransition(0, 0);
                } catch (Exception e) {
                    Log.e("starActivity", "启动 Sea_deluser_Activity 时出错: ", e);
                    Toast.makeText(starActivity.this, "启动查询/删除界面失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 为“添加用户信息”按钮设置点击监听器
        insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(starActivity.this, Insertuser_Activity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e("starActivity", "启动 Insertuser_Activity 时出错: ", e);
                    Toast.makeText(starActivity.this, "启动添加用户信息界面失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 你还可以为“修改用户信息”按钮（updata_bin）设置点击监听器，这里暂不实现
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sql_read != null && sql_read.isOpen()) {
            sql_read.close();
        }
        if (user != null) {
            user.close();
        }
    }
}