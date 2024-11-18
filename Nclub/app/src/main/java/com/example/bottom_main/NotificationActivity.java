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
        String[] data = {"已成功加入<跨年之旅>活動", "R發送「我迷路了」訊息", "R發送「SOS」訊息","R發送「我到了」訊息"};

        // 創建自定義適配器並設置給 ListView
        CustomAdapter adapter = new CustomAdapter(this, data);
        listView.setAdapter(adapter);
    }
}