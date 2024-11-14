package com.example.bottom_main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText loginUsername, loginPassword;
    Button loginButton;
    TextView signupRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        loginButton = findViewById(R.id.login_button);
//        // 檢查 SharedPreferences 中的登入狀態
//        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
//        String savedUsername = sharedPreferences.getString("username", null);
//        String savedPassword = sharedPreferences.getString("password", null);
//
//        if (savedUsername != null && savedPassword != null) {
//            // 如果有儲存的帳號和密碼，直接進入主畫面
//            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish(); // 結束歡迎畫面
//        } else {
        // 讀取儲存的帳號和密碼
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("username", "");
        String savedPassword = sharedPreferences.getString("password", "");
        if (savedUsername != null && savedPassword != null) {
            // 如果有儲存的帳號和密碼，直接帶入帳密登入
            loginUsername.setText(savedUsername);
            loginPassword.setText(savedPassword);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateUsername() || !validatePassword()) {
                    Toast.makeText(LoginActivity.this, "请检查用户名和密码", Toast.LENGTH_SHORT).show();
                } else {
                    checkUser();
                }
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean validateUsername() {
        String val = loginUsername.getText().toString().trim(); // 去除前后空格
        if (TextUtils.isEmpty(val)) {
            loginUsername.setError("未輸入使用者名稱!");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    public boolean validatePassword() {
        String val = loginPassword.getText().toString().trim(); // 去除前后空格
        if (TextUtils.isEmpty(val)) {
            loginPassword.setError("未輸入密碼!");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = HashUtil.hashPassword(loginPassword.getText().toString());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String passwordFromDB = userSnapshot.child("password").getValue(String.class);

                        if (passwordFromDB != null && passwordFromDB.equals(userPassword)) {
                            // 儲存帳號和密碼
                            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", userUsername);
                            editor.putString("password", loginPassword.getText().toString()); // 儲存密碼
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("username", userUsername);
                            intent.putExtra("email", userSnapshot.child("email").getValue(String.class));
                            intent.putExtra("userId", userSnapshot.child("userId").getValue(String.class));
                            startActivity(intent);
                            finish();
                            return;
                        } else {
                            loginPassword.setError("您輸入的密碼錯誤!");
                            loginPassword.requestFocus();
                            return;
                        }
                    }
                } else {
                    loginUsername.setError("使用者名稱不存在");
                    loginUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("LoginActivity", "Database error: " + error.getMessage());
            }
        });
    }

}
