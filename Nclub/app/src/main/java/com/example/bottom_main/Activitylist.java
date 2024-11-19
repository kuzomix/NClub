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
import java.util.List;

public class Activitylist extends AppCompatActivity {

    private String userId; // 從上個介面傳遞的 userId
    private RecyclerView activityRecyclerView;
    private ActivityListAdapter adapter;
    private List<String> activityTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitylist);

        // 接收從 Intent 傳遞的 userId
        userId = getIntent().getStringExtra("userId");
        if (userId == null) {
            Toast.makeText(this, "未接收到用戶 ID，無法加載活動列表", Toast.LENGTH_SHORT).show();
            finish(); // 若未傳遞 userId，直接結束 Activity
            return;
        }

        // 初始化 RecyclerView
        activityRecyclerView = findViewById(R.id.activityRecyclerView);
        activityRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 初始化活動列表和適配器
        activityTitles = new ArrayList<>();
        adapter = new ActivityListAdapter(this, activityTitles, position -> {
            String selectedActivity = activityTitles.get(position);
            Toast.makeText(Activitylist.this, "選擇的活動: " + selectedActivity, Toast.LENGTH_SHORT).show();
        });

        activityRecyclerView.setAdapter(adapter);

        // 加載用戶參加的活動
        loadUserActivities();
    }

    /**
     * 從 Firebase 加載用戶參加的活動列表
     */
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
                                // 根據 tourItemId 加載對應的活動資訊
                                loadActivityDetails(tourItemId);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Activitylist.this, "無法加載活動資訊", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 根據 tourItemId 加載活動詳細資訊
     *
     * @param tourItemId 活動 ID
     */
    private void loadActivityDetails(String tourItemId) {
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference("Items").child(tourItemId);
        itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot itemSnapshot) {
                String activityTitle = itemSnapshot.child("title").getValue(String.class);
                if (activityTitle != null) {
                    activityTitles.add(activityTitle); // 添加活動標題到列表
                    adapter.notifyDataSetChanged(); // 更新 RecyclerView
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Activitylist.this, "無法加載活動資訊", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
