package com.example.bottom_main.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottom_main.R;

import java.util.List;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ViewHolder> {
    private List<String> friendsList;

    public FriendsListAdapter(List<String> friendsList) {
        this.friendsList = friendsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String friendEmail = friendsList.get(position);
        holder.textViewFriendEmail.setText(friendEmail);
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFriendEmail;

        ViewHolder(View itemView) {
            super(itemView);
            textViewFriendEmail = itemView.findViewById(R.id.tv_friend_email); // 確保這個 ID 與你的布局匹配
        }
    }
}
