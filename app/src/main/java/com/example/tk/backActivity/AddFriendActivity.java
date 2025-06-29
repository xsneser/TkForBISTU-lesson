package com.example.tk.backActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tk.R;
import com.example.tk.testweb;
import com.example.tk.userDatabase.user_database;

public class AddFriendActivity extends AppCompatActivity {

    private EditText et;
    private Button btnadd;
    private Button btnaccept;
    private user_database userf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        userf = new user_database(this);

        et = findViewById(R.id.etFriendname);
        btnadd = findViewById(R.id.btnAddFriend);
        btnaccept = findViewById(R.id.btnAcceptRequest);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                boolean isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false);

                if(isLoggedIn){
                    String IsLOGIN;
                    String userid = sharedPreferences.getString("ID", "");
                    String fid = et.getText().toString().trim();
                    testweb inmessage = new testweb();
                    inmessage.toserve("A"+ fid +"|" + userid);
                    IsLOGIN = inmessage.outmessage;
                }
            }
        });
    }

}