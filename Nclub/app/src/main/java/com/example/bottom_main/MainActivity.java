package com.example.bottom_main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import com.example.bottom_main.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    String username, email, userId; // 將 username, email 和 userId 宣告為成員變數

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 使用 ActivityMainBinding 綁定佈局
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 獲取傳遞過來的資料
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        email = intent.getStringExtra("email");
        userId = intent.getStringExtra("userId");

        // 傳遞資料給 HomeFragment
        Bundle homeBundle = new Bundle();
        homeBundle.putString("username", username);
        homeBundle.putString("email", email);
        homeBundle.putString("userId", userId);
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(homeBundle);

        // 替換為 HomeFragment
        replaceFragment(homeFragment);

        // 設置底部導航視圖
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.home:
                    // 傳遞資料給 HomeFragment
                    selectedFragment = new HomeFragment();
                    selectedFragment.setArguments(homeBundle);
                    break;

                case R.id.calendar:
                    selectedFragment = new CalendarFragment();
                    break;

                case R.id.chat:
                    // 傳遞資料給 ChatFragment
                    selectedFragment = new ChatFragment();
                    selectedFragment.setArguments(homeBundle);
                    break;

                case R.id.account:
                    // 傳遞資料給 AccountFragment
                    selectedFragment = new AccountFragment();
                    selectedFragment.setArguments(homeBundle);
                    break;
            }

            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
            }

            return true;
        });

        // 設置 FloatingActionButton 的點擊事件
        binding.fab.setOnClickListener(view -> showBottomDialog());
    }

    // 替換 Fragment 的方法
    void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment); // 確保 frame_layout 存在
        fragmentTransaction.commit();
    }

    // 顯示底部對話框的方法
    private void showBottomDialog() {
        BottomDialogFragment dialogFragment = new BottomDialogFragment(username, userId);
        dialogFragment.show(getSupportFragmentManager(), "BottomDialog");
    }

    // 設置 FAB 的可見性
    public void setFabVisibility(int visibility) {
        binding.fab.setVisibility(visibility);
    }
}
