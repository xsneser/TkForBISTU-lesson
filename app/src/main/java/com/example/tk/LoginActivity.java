package com.example.tk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity{

    // 声明界面控件
    private TextInputLayout usernameTextInputLayout;
    private TextInputEditText usernameEditText;
    private TextInputLayout passwordTextInputLayout;
    private TextInputLayout confirmPasswordTextInputLayout;
    private TextInputEditText passwordEditText;
    private TextInputEditText confirmPasswordEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        // 绑定界面控件
        usernameTextInputLayout = findViewById(R.id.usernameTextInputLayout);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        confirmPasswordTextInputLayout = findViewById(R.id.confirmPasswordTextInputLayout);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.registerButton);

        // 为注册按钮设置点击事件
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 验证密码一致性
                if (validatePasswords()) {
                    // 密码一致，进行注册逻辑
                    Toast.makeText(LoginActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                    // 这里可以添加实际的注册逻辑，如调用API
                }
            }
        });
    }
    private boolean validatePasswords() {
        // 获取输入的密码
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // 清空之前的错误提示
        usernameTextInputLayout.setError(null);
        passwordTextInputLayout.setError(null);
        confirmPasswordTextInputLayout.setError(null);


        // 基本验证：密码不能为空
        if (password.isEmpty()) {
            passwordTextInputLayout.setError("请输入密码");
            return false;
        }
        if (confirmPassword.isEmpty()) {
            confirmPasswordTextInputLayout.setError("请确认密码");
            return false;
        }

        // 验证密码一致性
        if (!password.equals(confirmPassword)) {
            confirmPasswordTextInputLayout.setError("两次输入的密码不一致");
            return false;
        }

        // 密码长度验证示例（可根据需求调整）
        if (password.length() < 6) {
            passwordTextInputLayout.setError("密码长度至少为6位");
            return false;
        }

        // 用户名验证
        if (username.isEmpty()) {
            usernameTextInputLayout.setError("请输入用户名");
            return false;
        }

        // 所有验证通过
        return true;
    }
}

