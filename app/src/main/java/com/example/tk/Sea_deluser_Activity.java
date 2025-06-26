package com.example.tk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class Sea_deluser_Activity extends Activity {
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

        loadUserData();
        setupListViewAdapter();
        setupListViewClickListener();
    }

    private void loadUserData() {
        SQLiteDatabase db = null;
        try {
            db = userDB.getReadableDatabase();
            list = userDB.querydata(db);
            user_mes = new String[list.size()];

            for (int i = 0; i < list.size(); i++) {
                user_mes[i] = list.get(i).getUsername() + " " + list.get(i).getPaswd();
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

                new AlertDialog.Builder(Sea_deluser_Activity.this)
                        .setTitle("系统提示")
                        .setMessage("确定删除么")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteUser(userId);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                // 取消操作，无需处理
                            }
                        })
                        .show();
            }
        });
    }

    private void deleteUser(int userId) {
        SQLiteDatabase db = null;
        try {
            db = userDB.getWritableDatabase();
            userDB.delete(db, userId);
            refresh();
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "删除用户失败", Toast.LENGTH_SHORT).show();
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    private void refresh() {
        finish();
        Intent intent = new Intent(Sea_deluser_Activity.this, Sea_deluser_Activity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userDB != null) {
            userDB.close();
        }
    }
}