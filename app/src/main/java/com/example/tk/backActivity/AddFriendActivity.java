package com.example.tk.backActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.tk.R;
import com.example.tk.dao.FriendInfo;
import com.example.tk.testweb;
import com.example.tk.userDatabase.user_database;

import java.util.List;

public class AddFriendActivity extends AppCompatActivity {

    private EditText et;
    private Button btnadd;
    private Button btnaccept;
    private user_database userf;
    public SQLiteDatabase sql_read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        userf = new user_database(this);
        sql_read = userf.getReadableDatabase();

        et = findViewById(R.id.etFriendname);
        btnadd = findViewById(R.id.btnAddFriend);
        btnaccept = findViewById(R.id.btnAcceptRequest);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                boolean isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false);

                if (isLoggedIn) {
                    String userid = sharedPreferences.getString("ID", "");
                    String fid = et.getText().toString().trim();

                    if (isFriendExists(fid)) {
                        // 好友已存在，显示提示信息并返回上一页面
                        Toast.makeText(AddFriendActivity.this, "好友已存在", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // 好友不存在，继续添加好友的逻辑
                        testweb inmessage = new testweb();
                        inmessage.toserve("A" + fid + "|" + userid);
                        String IsLOGIN = inmessage.outmessage;
                        userf.add_f(sql_read, userid, fid);

                        // 刷新并回到上一个页面
                        NavController navController = Navigation.findNavController(v);
                        navController.navigateUp();
                    }
                } else {
                    // 用户未登录，显示提示信息
                    Toast.makeText(AddFriendActivity.this, "Please log in first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理接受好友请求的逻辑
                // ...
            }
        });
    }

    private boolean isFriendExists(String friendname) {
        List<FriendInfo> friends = userf.query_f(sql_read);
        for (FriendInfo friend : friends) {
            if (friend.getFriendname().equals(friendname)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sql_read != null && sql_read.isOpen()) {
            sql_read.close();
        }
        if (userf != null) {
            userf.close();
        }
    }
}