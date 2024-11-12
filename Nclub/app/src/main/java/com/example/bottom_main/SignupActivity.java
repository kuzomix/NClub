package com.example.bottom_main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {

    EditText signupName, signupEmail, signupUsername, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton = findViewById(R.id.signup_button);

        signupButton.setOnClickListener(v -> {
            String name = signupName.getText().toString();
            String email = signupEmail.getText().toString();
            String username = signupUsername.getText().toString();
            String password = signupPassword.getText().toString().trim();
            String createdAt = String.valueOf(System.currentTimeMillis());
            String lastLogin = String.valueOf(System.currentTimeMillis());

            // 針對四個欄位去做檢查
            if (!validateName() || !validateUserEmail() || !validateUsername() || !validatePassword()) {
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(SignupActivity.this, "請輸入有效的電子郵件地址", Toast.LENGTH_LONG).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(SignupActivity.this, "密碼至少需要 6 個字元", Toast.LENGTH_LONG).show();
                return;
            }


            // 哈希密碼
            String hashedPassword = HashUtil.hashPassword(password);
            Log.d("Debug", "SIGNUP hashedPassword: " + hashedPassword);

            // 儲存使用者資料到 Firebase
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

            // 检查用户名是否已存在
            reference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) { // 检查用户名是否已被注册
                        Toast.makeText(SignupActivity.this, "帳號已被註冊", Toast.LENGTH_SHORT).show();
                    } else {
                        // 用户名可用，进行注册
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        // 注册成功，获取当前用户
                                        FirebaseUser user = auth.getCurrentUser();
                                        if (user != null) {
                                            String userId = "user_" + user.getUid(); // 使用 Firebase UID 作为用户 ID
                                            Log.d("Debug", "SIGNUP userId: " + userId);

                                            // 创建 HelperClass 并保存用户数据
                                            HelperClass helperClass = new HelperClass(name, email, username, hashedPassword, createdAt, lastLogin, userId);
                                            reference.child(userId).setValue(helperClass).addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    Toast.makeText(SignupActivity.this, "你註冊成功!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    Log.d("MainActivity", "新增資料失敗.", task1.getException());
                                                }
                                            });
                                        }
                                    } else {
                                        Toast.makeText(SignupActivity.this, "註冊失敗: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // 处理错误
                }
            });
        });
        loginRedirectText.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    public Boolean validateName()
    {
        String val = signupName.getText().toString();
        if(val.isEmpty())
        {
            signupName.setError("未輸入註冊名字!");
            return false;
        }
        else
        {
            signupName.setError(null);
            return true;
        }
    }
    public Boolean validateUserEmail()
    {
        String val = signupEmail.getText().toString();
        if(val.isEmpty())
        {
            signupEmail.setError("未輸入註冊信箱!");
            return false;
        }
        else
        {
            signupEmail.setError(null);
            return true;
        }
    }
    public Boolean validateUsername()
    {
        String val = signupUsername.getText().toString();
        if(val.isEmpty())
        {
            signupUsername.setError("未輸入註冊使用者名稱!");
            return false;
        }
        else
        {
            signupEmail.setError(null);
            return true;
        }
    }

    public Boolean validatePassword()
    {
        String val = signupPassword.getText().toString();
        if(val.isEmpty())
        {
            signupPassword.setError("未輸入註冊密碼!");
            return false;
        }
        else
        {
            signupPassword.setError(null);
            return true;
        }
    }


}