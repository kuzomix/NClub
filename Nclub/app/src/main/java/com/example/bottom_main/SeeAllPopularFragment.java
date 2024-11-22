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

public class SeeAllPopularFragment extends Fragment {
    private EventAdapter eventAdapter;
    private RecyclerView recyclerView;
    private List<Event> recommendedEvents = new ArrayList<>();
    private String userId;

    // 新增構造函數
    public SeeAllPopularFragment(String userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_recommended, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        // 設置 GridLayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        int spanCount = 2; // 列數
        int spacing = 16; // 間距（像素）
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing));

        // 初始化適配器
        eventAdapter = new EventAdapter(recommendedEvents, getContext());
        recyclerView.setAdapter(eventAdapter);

        // 獲取推薦活動
        getAllRecommendedEvents();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setFabVisibility(View.GONE); // 隱藏 FAB
        }
    }

    private void getAllRecommendedEvents() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Items");

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recommendedEvents.clear(); // 清空舊資料
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.getKey();
                    String title = snapshot.child("title").getValue(String.class);
                    String imageUrl = snapshot.child("pic").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);
                    String userId = snapshot.child("userId").getValue(String.class);
                    boolean popularFlag = snapshot.child("popularFlag").getValue(Boolean.class);

                    if (title != null && imageUrl != null && description != null && popularFlag) {
                        Event event = new Event(id, title, imageUrl, description, userId);
                        recommendedEvents.add(event);
                        Log.d("Firebase", "Loaded event: " + recommendedEvents.size());
                    }
                }
                Log.d("Firebase", "Loaded events: " + recommendedEvents.size());
                if (recommendedEvents.isEmpty()) {
                    Log.d("Firebase", "No events found in database.");
                }
                // 通知適配器數據已更改
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });
    }
}
