package com.example.tk;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Insertuser_Activity extends Activity {
    private EditText name_edit,paswd_edit;
    private Spinner spinner;
    private Button save_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.user_inset);
        // 调用 init 方法
        init();
    }
    public void init(){
        name_edit=(EditText) findViewById(R.id.insert_name);
        paswd_edit=(EditText) findViewById(R.id.insert_paswd);
        save_btn=(Button) findViewById(R.id.save_usermes);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name_str=name_edit.getText().toString();
                String paswd_str=paswd_edit.getText().toString();
                user_database us_db=new user_database(Insertuser_Activity.this);
                SQLiteDatabase sqLiteDatabase = us_db.getWritableDatabase();
                us_db.adddata(sqLiteDatabase,name_str,paswd_str);
                Intent intent = new Intent(Insertuser_Activity.this, Sea_deluser_Activity.class);
                startActivity(intent);
            }
        });
    }
}