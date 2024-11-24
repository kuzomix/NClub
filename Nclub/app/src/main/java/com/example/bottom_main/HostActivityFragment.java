package com.example.bottom_main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bottom_main.Adapter.HostActivityAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HostActivityFragment extends Fragment {

    private String userId;
    private ListView activityListView;
    private ArrayList<String> activityTitles;  // 用來存儲活動標題的列表
    private ArrayList<String> chatroomIds;  // 用來存儲聊天室ID的列表
    private HostActivityAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.host_activity, container, false);

        // 接收 userId
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
        }

        Log.e("Debug", "HostActivityFragment - userId: " + userId);

        activityListView = view.findViewById(R.id.hostRecyclerView);
        activityTitles = new ArrayList<>();
        chatroomIds = new ArrayList<>();

        // 初始化適配器
        adapter = new HostActivityAdapter(getActivity(), activityTitles);
        activityListView.setAdapter(adapter);

        // 加載用戶創建的聊天室（即活動）
        loadHostActivities();

        return view;
    }

    // 加載用戶創建的活動（即聊天室）
    private void loadHostActivities() {
        // 根據 userId 查找該用戶所創建的所有聊天室
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("chatrooms");
        databaseReference.orderByChild("creatorId").equalTo(userId)  // 根據創建者ID篩選
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        activityTitles.clear();
                        chatroomIds.clear();  // 清空舊的資料

                        // 遍歷每一個聊天室
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            chatroomIds.add(snapshot.getKey());  // 獲取聊天室ID

                            // 獲取活動標題
                            String activityTitle = snapshot.child("activityTitle").getValue(String.class);

                            if (activityTitle != null) {
                                activityTitles.add(activityTitle);  // 將活動標題加入列表
                            }
                        }

                        // 更新適配器
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "無法加載活動資料", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
