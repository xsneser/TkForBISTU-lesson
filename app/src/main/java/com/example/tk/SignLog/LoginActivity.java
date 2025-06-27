package com.example.tk.SignLog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;

import com.example.tk.R;
import com.example.tk.dao.UserInfo;
import com.example.tk.userDatabase.user_database;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class LoginActivity extends AppCompatActivity{

    // 声明界面控件
    private TextInputLayout usernameTextInputLayout;
    private TextInputEditText usernameEditText;
    private TextInputLayout passwordTextInputLayout;

    private TextInputEditText passwordEditText;

    private Button registerButton;
    private user_database userDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        userDB = new user_database(this);

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
                    int a=checkLogin();
                if (validatePasswords()&&a!=0) {
                    // 密码一致，进行注册逻辑
                    Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                    // 保存登录状态和用户名
                    SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("is_logged_in", true);
                    editor.putString("username", usernameEditText.getText().toString().trim());
                    editor.putString("ID",String.valueOf(a));
                    editor.apply();

                    finish();
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

        // 基本验证：用户名和密码不能为空
        if (username.isEmpty()) {
            usernameTextInputLayout.setError("请输入用户名");
            return false;
        }
        if (password.isEmpty()) {
            passwordTextInputLayout.setError("请输入密码");
            return false;
        }

        // 所有验证通过
        return true;
    }

    private int checkLogin() {
        passwordTextInputLayout.setError(null);
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // 对输入的密码进行哈希处理
        String hashedInputPassword = hashPassword(password);
        if (hashedInputPassword == null) {
            // 处理哈希失败的情况
            passwordTextInputLayout.setError("系统错误，请重试");
            return 0;
        }

        List<UserInfo> userList = userDB.querydata(null);
        for (UserInfo user : userList) {
            // 比较用户名和哈希后的密码
            if (user.getUsername().equals(username) &&
                    user.getPaswd().equals(hashedInputPassword)) {
                return user.getId();
            }
        }

        passwordTextInputLayout.setError("密码或用户名错误");
        return 0;
    }

    /**
     * 使用SHA-256算法对密码进行哈希（不加盐）
     * @param password 原始密码
     * @return 哈希后的密码（十六进制字符串），出错时返回null
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}

