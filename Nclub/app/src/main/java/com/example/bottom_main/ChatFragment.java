package com.example.bottom_main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    private String userId;
    private ListView chatroomListView;
    private ArrayList<String> chatroomIds; // 聊天室 ID 列表

    public ChatFragment(Bundle chatArgs) {
        if (chatArgs != null) {
            userId = chatArgs.getString("userId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // 獲取傳遞過來的 userId
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
        }

        chatroomListView = view.findViewById(R.id.chatroomListView);
        chatroomIds = new ArrayList<>();

        // 加載聊天室 ID
        loadChatrooms();

        // 設置聊天室選擇的點擊事件
        chatroomListView.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedChatroomId = chatroomIds.get(position);
            // 進入選擇的聊天室
            enterChatroom(selectedChatroomId);
        });

        return view;
    }

    private void loadChatrooms() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("chatrooms");
        databaseReference.orderByChild("members/" + userId).equalTo(true)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        chatroomIds.clear(); // 清空舊的聊天室 ID
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            chatroomIds.add(snapshot.getKey()); // 獲取聊天室 ID
                        }
                        // 更新 ListView
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, chatroomIds);
                        chatroomListView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "無法加載聊天室", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void enterChatroom(String chatroomId) {
        ChatDetailFragment chatDetailFragment = new ChatDetailFragment();
        Bundle args = new Bundle();
        args.putString("chatroomId", chatroomId);
        chatDetailFragment.setArguments(args);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, chatDetailFragment)
                .addToBackStack(null)
                .commit();

        // 加載聊天消息
        chatDetailFragment.loadChatMessages(chatroomId); // 確保在 ChatDetailFragment 中有這個方法
    }
}
