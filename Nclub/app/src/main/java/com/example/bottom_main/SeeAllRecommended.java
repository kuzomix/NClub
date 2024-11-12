package com.example.bottom_main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.*;

public class SeeAllRecommended extends AppCompatActivity {
    private EventAdapter eventAdapter;
    private RecyclerView recyclerView;
    private List<Event> recommendedEvents = new ArrayList<>(); // 初始化類別變量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_recommended);

        recyclerView = findViewById(R.id.recyclerView);

        // 設置 GridLayoutManager，每行顯示兩個項目
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        int spanCount = 2; // 列數
        int spacing = 16; // 間距（像素）
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing));

        // 初始化適配器並設置 RecyclerView
        eventAdapter = new EventAdapter(recommendedEvents, this);
        recyclerView.setAdapter(eventAdapter);

        // 呼叫方法取得推薦活動
        getAllRecommendedEvents();
    }

    private void getAllRecommendedEvents() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Items");

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recommendedEvents.clear(); // 清空舊資料，避免重複
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.getKey();
                    String title = snapshot.child("title").getValue(String.class);
                    String imageUrl = snapshot.child("pic").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);
                    Log.d("Firebase", "title imageUrl description: " + title + ";" + imageUrl + ";" + description);

                    if (title != null && imageUrl != null && description != null) {
                        Event event = new Event(id,title, imageUrl, description);
                        recommendedEvents.add(event);
                        Log.d("Firebase", "Loaded event: " + recommendedEvents.size());

                    }
                }
                // 確認是否讀取到資料
                Log.d("Firebase", "Loaded events: " + recommendedEvents.size());
                if (recommendedEvents.isEmpty()) {
                    Log.d("Firebase", "No events found in database.");
                }
                // 更新 RecyclerView 資料
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });
    }
}
