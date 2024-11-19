package com.example.bottom_main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class FollowActivityFragment extends Fragment {
    private EventAdapter eventAdapter;
    private RecyclerView recyclerView;
    private List<Event> recommendedEvents = new ArrayList<>(); // 初始化類別變量
    private String userId;

    // 新增構造函數
    public FollowActivityFragment(String userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 加載佈局
        View view = inflater.inflate(R.layout.activity_follow, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        // 設置 GridLayoutManager，每行顯示兩個項目
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        int spanCount = 2; // 列數
        int spacing = 16; // 間距（像素）
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing));

        // 初始化適配器並設置 RecyclerView
        eventAdapter = new EventAdapter(recommendedEvents, getContext());
        recyclerView.setAdapter(eventAdapter);

        // 呼叫方法取得推薦活動
        getTourItemIds();

        return view;
    }

    private void getTourItemIds() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        // 獲取特定用戶的關注項目
        usersRef.child(userId).child("follow").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> tourItemIds = new ArrayList<>(); // 用於存儲符合條件的 tourItemId
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getValue(Boolean.class)) {
                        String itemId = snapshot.getKey(); // 獲取關注的項目 ID
                        Log.d("FollowActivityFragment", "已關注的活動: " + itemId);
                        if (itemId != null) {
                            getRecommendedEvents(itemId); // 收集符合條件的 tourItemId
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching user follows", databaseError.toException());
            }
        });
    }

    private void getRecommendedEvents(String tourItemId) {
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference("Items");

        itemsRef.orderByKey().equalTo(tourItemId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.getKey();
                    String title = snapshot.child("title").getValue(String.class);
                    String imageUrl = snapshot.child("pic").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);

                    if (title != null && imageUrl != null && description != null) {
                        Event event = new Event(id, title, imageUrl, description, userId);
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
                Log.e("Firebase", "Error fetching items", databaseError.toException());
            }
        });
    }
}
