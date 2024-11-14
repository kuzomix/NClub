package com.example.bottom_main;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class ChatDetailFragment extends Fragment {
    private String chatroomId;
    private String userId; // 用戶 ID
    private String username; // 用戶名稱
    private RecyclerView chatRecyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private EditText messageInput;
    private Button sendButton;

    // 用戶顏色緩存
    private Map<String, Integer> userColorMap = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_detail, container, false);

        if (getArguments() != null) {
            chatroomId = getArguments().getString("chatroomId");
            userId = getArguments().getString("userId");
            username = getArguments().getString("username");
        }

        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        messageList = new ArrayList<>();

        messageAdapter = new MessageAdapter(messageList, userId);
        chatRecyclerView.setAdapter(messageAdapter);

        messageInput = view.findViewById(R.id.messageInput);
        sendButton = view.findViewById(R.id.sendButton);
        Log.e("Debug", "ChatDetailFragment- userId: " + userId);

        loadChatMessages(chatroomId, userId);

        sendButton.setOnClickListener(v -> {
            String messageText = messageInput.getText().toString();
            if (!messageText.isEmpty()) {
                sendMessage(messageText);
                messageInput.setText(""); // 清空輸入框
            }
        });

        return view;
    }

    public void loadChatMessages(String chatroomId, String userId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("chatrooms").child(chatroomId).child("messages");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear(); // 清空舊的消息
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    String messageText = messageSnapshot.child("text").getValue(String.class);
                    String sender = messageSnapshot.child("sender").getValue(String.class);
                    String senderId = messageSnapshot.child("userId").getValue(String.class);
                    Log.e("Debug", "loadChatMessages- senderId: " + senderId);
                    Log.e("Debug", "loadChatMessages- userId: " + userId);

                    messageList.add(new Message(sender, senderId, messageText, userId));
                }
                messageAdapter.notifyDataSetChanged(); // 更新適配器
                chatRecyclerView.scrollToPosition(messageList.size() - 1); // 滾動到最新消息
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "無法加載消息", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage(String messageText) {
        DatabaseReference messageRef = FirebaseDatabase.getInstance().getReference("chatrooms").child(chatroomId).child("messages").push();
        messageRef.setValue(new Message(username, userId, messageText, userId)); // 假設有一個 Message 類別
    }

    private String formatTimestamp(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date(Long.parseLong(timestamp)));
    }

    // 隨機生成顏色的方法
    private int getRandomColor() {
        Random random = new Random();
        return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    // 獲取用戶顏色
    private int getUserColor(String username) {
        if (!userColorMap.containsKey(username)) {
            userColorMap.put(username, getRandomColor()); // 為新用戶生成顏色
        }
        return userColorMap.get(username); // 返回用戶的顏色
    }
}
