package com.example.tk.SignLog;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tk.R;
import com.example.tk.userDatabase.user_database;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignupActivity extends AppCompatActivity{

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
        setContentView(R.layout.sign_up);

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
                    init();
                }
            }
        });

        TextView rebackView = findViewById(R.id.backToLoginTextView);
        rebackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

    public void init(){
        EditText name_edit = findViewById(R.id.usernameEditText);
        EditText paswd_edit = findViewById(R.id.passwordEditText);
        String name_str = name_edit.getText().toString();
        String password = paswd_edit.getText().toString();

        // 对密码进行哈希处理
        String hashedPassword = hashPassword(password);
        if (hashedPassword == null) {
            Toast.makeText(this, "密码加密失败，请重试", Toast.LENGTH_SHORT).show();
            return;
        }

        user_database us_db = new user_database(SignupActivity.this);
        SQLiteDatabase sqLiteDatabase = us_db.getWritableDatabase();
        us_db.adddata(sqLiteDatabase, name_str, hashedPassword);
        Toast.makeText(SignupActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * 使用SHA-256算法对密码进行哈希（不加盐）
     * @param password 原始密码
     * @return 哈希后的密码（十六进制字符串），出错时返回null
     */
    private String hashPassword(String password) {
        try {
            // 创建MessageDigest实例，指定SHA-256算法
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // 计算哈希值
            byte[] hashBytes = digest.digest(password.getBytes());

            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                // 将每个字节转换为两位十六进制表示
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0'); // 补零
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // 处理算法不可用的情况
            e.printStackTrace();
            return null;
        }
    }
}