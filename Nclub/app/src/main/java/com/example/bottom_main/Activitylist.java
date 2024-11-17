package com.example.bottom_main;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottom_main.Adapter.ActivityListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activitylist extends AppCompatActivity {

    private String userId; // 從上一個 Activity 或 Fragment 傳遞過來的用戶 ID
    private RecyclerView recyclerView;
    private ActivityListAdapter adapter;
    private ArrayList<String> activityTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitylist);

        // 獲取從上一個介面傳遞的用戶 ID
        userId = getIntent().getStringExtra("userId");

        recyclerView = findViewById(R.id.activityRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        activityTitles = new ArrayList<>();

        // 初始化適配器
        adapter = new ActivityListAdapter(this, activityTitles, position -> {
            String selectedActivity = activityTitles.get(position);
            Toast.makeText(Activitylist.this, "Selected: " + selectedActivity, Toast.LENGTH_SHORT).show();
        });

        recyclerView.setAdapter(adapter);

        // 加載用戶參加的活動
        loadUserActivities();
    }

    private void loadUserActivities() {
        DatabaseReference chatroomsRef = FirebaseDatabase.getInstance().getReference("chatrooms");
        chatroomsRef.orderByChild("members/" + userId).equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        activityTitles.clear(); // 清空舊資料
                        for (DataSnapshot chatroomSnapshot : dataSnapshot.getChildren()) {
                            String tourItemId = chatroomSnapshot.child("tourItemId").getValue(String.class);

                            if (tourItemId != null) {
                                // 查詢活動資訊
                                loadActivityDetails(tourItemId);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Activitylist.this, "無法加載用戶活動", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadActivityDetails(String tourItemId) {
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference("Items").child(tourItemId);
        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot itemSnapshot) {
                String activityTitle = itemSnapshot.child("title").getValue(String.class);
                if (activityTitle != null) {
                    activityTitles.add(activityTitle); // 添加活動標題
                    adapter.notifyDataSetChanged(); // 更新 ListView
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Activitylist.this, "無法加載活動資訊", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
