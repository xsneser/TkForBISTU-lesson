package com.example.tk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity{

    // 声明界面控件
    private TextInputLayout usernameTextInputLayout;
    private TextInputEditText usernameEditText;
    private TextInputLayout passwordTextInputLayout;

    private TextInputEditText passwordEditText;

    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        // 绑定界面控件
        usernameTextInputLayout = findViewById(R.id.usernameTextInputLayout);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);

        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.loginButton);

        // 为注册按钮设置点击事件
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validatePasswords()) {
                    // 密码一致，进行注册逻辑


                    Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        TextView logView = findViewById(R.id.goToSignupTextView);
        logView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });


    }
    private boolean validatePasswords() {
        // 获取输入的密码
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();


        // 清空之前的错误提示
        usernameTextInputLayout.setError(null);
        passwordTextInputLayout.setError(null);



        // 基本验证：密码不能为空
        if (password.isEmpty()) {
            // 密码长度验证示例（可根据需求调整）
            if (password.length() < 6) {
                passwordTextInputLayout.setError("密码错误");
                return false;
            }

            // 用户名验证
            if (username.isEmpty()) {
                usernameTextInputLayout.setError("请输入用户名");
                return false;
            }
            passwordTextInputLayout.setError("请输入密码");
            return false;
        }




        // 所有验证通过
        return true;
    }
}

