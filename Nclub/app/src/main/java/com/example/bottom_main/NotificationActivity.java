package com.example.bottom_main;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bottom_main.Adapter.CustomAdapter;

public class NotificationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ListView listView = findViewById(R.id.listview);

        // 示例數據
        String[] data = {"項目 1", "項目 2", "項目 3"};

        // 創建自定義適配器並設置給 ListView
        CustomAdapter adapter = new CustomAdapter(this, data);
        listView.setAdapter(adapter);
    }
}