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
import com.example.tk.SignLog.Sea_deluser_Activity;
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