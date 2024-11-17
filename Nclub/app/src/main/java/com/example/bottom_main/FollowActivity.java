package com.example.bottom_main;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;
import com.squareup.picasso.Picasso; // 用來加載圖片

import java.util.ArrayList;
import java.util.List;

public class FollowActivity extends AppCompatActivity {

    private RecyclerView followedEventsRecyclerView;
    private EventAdapter adapter;
    private List<Event> followedEvents = new ArrayList<>(); // 用來存儲已關注的活動

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        followedEventsRecyclerView = findViewById(R.id.followedEventsRecyclerView);

        // 設定 GridLayoutManager 顯示為兩列
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        followedEventsRecyclerView.setLayoutManager(gridLayoutManager);

        // 初始化適配器並設置給 RecyclerView
        adapter = new EventAdapter(followedEvents, this);
        followedEventsRecyclerView.setAdapter(adapter);

        // 從 Firebase 加載已關注的活動
        loadFollowedEvents();
    }

    private void loadFollowedEvents() {
        DatabaseReference followsRef = FirebaseDatabase.getInstance().getReference("Follows");

        // 從 Firebase 資料庫讀取已關注的活動
        followsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                followedEvents.clear(); // 清空舊的資料，避免重複
                for (DataSnapshot followSnapshot : dataSnapshot.getChildren()) {
                    Boolean isFollowed = followSnapshot.getValue(Boolean.class);
                    if (isFollowed != null && isFollowed) {
                        String activityId = followSnapshot.getKey();

                        // 根據活動 ID 取得詳細資料
                        fetchActivityDetails(activityId);
                    }
                }

                // 如果列表是空的，顯示提示
                if (followedEvents.isEmpty()) {
                    Toast.makeText(FollowActivity.this, "您尚未關注任何活動", Toast.LENGTH_SHORT).show();
                }

                // 更新 RecyclerView
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "讀取已關注的活動失敗", databaseError.toException());
                Toast.makeText(FollowActivity.this, "讀取已關注的活動失敗", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchActivityDetails(String activityId) {
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference("Items");
        itemsRef.child(activityId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title = dataSnapshot.child("title").getValue(String.class);
                String imageUrl = dataSnapshot.child("pic").getValue(String.class);
                String description = dataSnapshot.child("description").getValue(String.class);

                Log.d("Firebase", "標題: " + title + "; 圖片: " + imageUrl + "; 描述: " + description);

                if (title != null && imageUrl != null && description != null) {
                    Event event = new Event(activityId, title, imageUrl, description);
                    followedEvents.add(event);
                    Log.d("Firebase", "已載入關注的活動: " + event.getTitle());
                }

                // 更新 RecyclerView (這裡的 notifyDataSetChanged 會批量更新)
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "獲取活動詳細資料失敗", databaseError.toException());
                Toast.makeText(FollowActivity.this, "獲取活動詳細資料失敗", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
