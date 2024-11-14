package com.example.bottom_main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    private String userId, username, email;
    private ListView chatroomListView;
    private ArrayList<String> chatroomIds; // 聊天室 ID 列表
    private ArrayList<String> chatroomTitles; // 聊天室標題列表
    private ArrayAdapter<String> adapter; // 適配器


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        // 接收資料
        Bundle bundle = getArguments();
        if (bundle != null) {
            username = bundle.getString("username");
            email = bundle.getString("email");
            userId = bundle.getString("userId");
        }

        chatroomListView = view.findViewById(R.id.chatroomListView);
        chatroomIds = new ArrayList<>();
        chatroomTitles = new ArrayList<>(); // 初始化標題列表

        // 初始化適配器
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, chatroomTitles);
        chatroomListView.setAdapter(adapter);

        // 加載聊天室 ID
        loadChatrooms();

        // 設置聊天室選擇的點擊事件
        chatroomListView.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedChatroomId = chatroomIds.get(position);
            String selectedTitle = chatroomTitles.get(position); // 獲取選擇的標題
            // 進入選擇的聊天室
            enterChatroom(selectedChatroomId, selectedTitle);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setFabVisibility(View.GONE); // 隱藏 FAB
        }
    }

    private void loadChatrooms() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("chatrooms");
        databaseReference.orderByChild("members/" + userId).equalTo(true)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        chatroomIds.clear();
                        chatroomTitles.clear(); // 清空舊的標題
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            chatroomIds.add(snapshot.getKey()); // 獲取聊天室 ID
                            String title = snapshot.child("tourItemId").getValue(String.class); // 獲取聊天室標題
                            if (title != null) {
                                // 根據 tourItemId 獲取對應的 title
                                DatabaseReference itemRef = FirebaseDatabase.getInstance().getReference("Items").child(title);
                                itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot itemSnapshot) {
                                        String itemTitle = itemSnapshot.child("title").getValue(String.class);
                                        chatroomTitles.add(itemTitle); // 添加標題到列表
                                        // 更新 ListView
                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, chatroomTitles);
                                        chatroomListView.setAdapter(adapter);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(getActivity(), "無法加載聊天室標題", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "無法加載聊天室", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void enterChatroom(String chatroomId, String title) {
        ChatDetailFragment chatDetailFragment = new ChatDetailFragment();
        Bundle args = new Bundle();
        args.putString("chatroomId", chatroomId);
        args.putString("title", title); // 傳遞標題
        args.putString("userId", userId);
        args.putString("username", username);
        chatDetailFragment.setArguments(args);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, chatDetailFragment)
                .addToBackStack(null)
                .commit();

        // 加載聊天消息
        chatDetailFragment.loadChatMessages(chatroomId, userId); // 確保在 ChatDetailFragment 中有這個方法
    }
}
