package com.example.bottom_main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FollowedEventsAdapter extends RecyclerView.Adapter<FollowedEventsAdapter.ViewHolder> {

    private final List<Event> followedEventsList;

    public FollowedEventsAdapter(List<Event> followedEventsList) {
        this.followedEventsList = followedEventsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_followed_events_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = followedEventsList.get(position);
        holder.titleTextView.setText(event.getTitle());
        holder.descriptionTextView.setText(event.getDescription());

        // 使用 Picasso 加載圖片
        Picasso.get().load(event.getImageUrl()).into(holder.eventImageView);
    }

    @Override
    public int getItemCount() {
        return followedEventsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView descriptionTextView;
        public ImageView eventImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.eventTitle);
            descriptionTextView = itemView.findViewById(R.id.eventDescription);
            eventImageView = itemView.findViewById(R.id.eventImage); // 用來顯示圖片
        }
    }
}
