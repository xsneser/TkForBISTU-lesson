package com.example.tk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class UserMainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        LinearLayout messageView = findViewById(R.id.linearLayout2);
        messageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMainActivity.this, UserMainMessageActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                //finish();
            }
        });

        LinearLayout newView = findViewById(R.id.linearLayout3);
        newView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMainActivity.this, UserMainNewActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                //finish();
            }
        });
        LinearLayout meView = findViewById(R.id.linearLayout4);
        meView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserMainActivity.this, UserMainMeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                //finish();
            }
        });

    }
}
