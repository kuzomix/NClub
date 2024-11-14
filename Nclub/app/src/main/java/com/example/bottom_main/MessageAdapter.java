package com.example.bottom_main;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> messages;
    private String currentUserId;

    public MessageAdapter(List<Message> messages, String currentUserId) {
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.messageText.setText(message.getText());
        Log.e("Debug", "MessageAdapter- currentUserId: " + currentUserId);

        // 設置對齊方式
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.messageText.getLayoutParams();
        if (message.getSenderId().equals(currentUserId)) {
            layoutParams.gravity = Gravity.END; // 右對齊
            holder.messageText.setBackgroundResource(R.drawable.bg_message_right); // 右邊背景
            Log.e("Debug", "MessageAdapter- 右對齊: " + message.getSenderId());
        } else {
            layoutParams.gravity = Gravity.START; // 左對齊
            holder.messageText.setBackgroundResource(R.drawable.bg_message_left); // 左邊背景
            Log.e("Debug", "MessageAdapter- 左對齊: " + message.getSenderId());
        }
        holder.messageText.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
        }
    }
}
