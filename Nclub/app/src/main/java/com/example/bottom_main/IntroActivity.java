package com.example.bottom_main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.example.bottom_main.databinding.ActivityIntroBinding;
import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {
    ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
            // 否則顯示登入畫面
            binding.introBtn.setOnClickListener(view -> startActivity(new Intent(IntroActivity.this, LoginActivity.class)));
//        }
    }
}
