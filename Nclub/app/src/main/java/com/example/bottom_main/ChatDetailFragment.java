package com.example.bottom_main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatDetailFragment extends Fragment {

    private String chatroomId;
    private String userId; // 用戶 ID
    private TextView chatDisplay;
    private EditText messageInput;
    private Button sendButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_detail, container, false);

        if (getArguments() != null) {
            chatroomId = getArguments().getString("chatroomId");
            userId = getArguments().getString("userId"); // 獲取 userId
        }

        chatDisplay = view.findViewById(R.id.chatDisplay);
        messageInput = view.findViewById(R.id.messageInput);
        sendButton = view.findViewById(R.id.sendButton);

        loadChatMessages(chatroomId);

        sendButton.setOnClickListener(v -> {
            String messageText = messageInput.getText().toString();
            if (!messageText.isEmpty()) {
                sendMessage(messageText);
                messageInput.setText(""); // 清空輸入框
            }
        });

        return view;
    }

    public void loadChatMessages(String chatroomId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("chatrooms").child(chatroomId).child("messages");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StringBuilder chatMessages = new StringBuilder();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    String messageText = messageSnapshot.child("text").getValue(String.class);
                    String senderId = messageSnapshot.child("sender").getValue(String.class);
                    chatMessages.append(senderId).append(": ").append(messageText).append("\n");
                }
                chatDisplay.setText(chatMessages.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "無法加載消息", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage(String messageText) {
        DatabaseReference messageRef = FirebaseDatabase.getInstance().getReference("chatrooms").child(chatroomId).child("messages").push();
        messageRef.setValue(new Message(userId, messageText)); // 假設有一個 Message 類別
    }
}
