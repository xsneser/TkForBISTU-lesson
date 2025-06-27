package com.example.tk.SignLog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tk.R;
import com.example.tk.activity.UserMainMeActivity;
import com.example.tk.dao.UserInfo;
import com.example.tk.userDatabase.user_database;

import java.util.List;

public class choiseUser_Activity extends Activity {
    private ListView user_list;
    private List<UserInfo> list;
    private user_database userDB;
    private String[] user_mes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.user_search_delete);

        user_list = findViewById(R.id.mes);
        userDB = new user_database(this);

        LinearLayout loginView = findViewById(R.id.top_bar);
        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(choiseUser_Activity.this,    LoginActivity.class);
                startActivity(intent);
            }
        });

        loadUserData();
        setupListViewAdapter();
        setupListViewClickListener();
    }
    protected void onRestart() {
        loadUserData();
        setupListViewAdapter();
        setupListViewClickListener();
        super.onRestart();
    }

    private void loadUserData() {
        SQLiteDatabase db = null;
        try {
            db = userDB.getReadableDatabase();
            list = userDB.querydata(db);
            user_mes = new String[list.size()];

            for (int i = 0; i < list.size(); i++) {
                user_mes[i] = list.get(i).getUsername() + " UID:" + list.get(i).getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "加载用户数据失败", Toast.LENGTH_SHORT).show();
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    private void setupListViewAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                user_mes
        );
        user_list.setAdapter(adapter);
    }

    private void setupListViewClickListener() {
        user_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final int userId = list.get(position).getId();
                SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("is_logged_in", true);
                editor.putString("username",list.get(position).getUsername());
                editor.putString("ID",String.valueOf(userId));
                editor.apply();
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userDB != null) {
            userDB.close();
        }
    }
}