package com.example.tk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class UserMainMessageActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_message_layout);

        LinearLayout messageView = findViewById(R.id.linearLayout1);
        messageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMainMessageActivity.this, UserMainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        LinearLayout newView = findViewById(R.id.linearLayout3);
        newView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMainMessageActivity.this, UserMainNewActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
        LinearLayout meView = findViewById(R.id.linearLayout4);
        meView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMainMessageActivity.this, UserMainMeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }
}
