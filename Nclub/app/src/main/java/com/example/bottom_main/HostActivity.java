package com.example.bottom_main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class HostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_activity);

        // 創建 Fragment 實例
        Fragment hostActivityFragment = new HostActivityFragment();

        // 使用 FragmentTransaction 加載 Fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, hostActivityFragment); // 將 Fragment 加入容器
        transaction.addToBackStack(null); // 可選的，允許返回到上一個 Fragment
        transaction.commit(); // 提交事務
    }
}
