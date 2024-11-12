package com.example.bottom_main;

import android.content.Context;   //11.11new
import android.content.Intent;   //11.11new
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // 請確保你已添加 Glide 的依賴

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event> recommendedEvents;
    private Context context;  //11.11new

    public EventAdapter(List<Event> recommendedEvents, Context context) {   //11.11new
        this.recommendedEvents = recommendedEvents;
        this.context = context;  //11.11new
    }

    // 定義 ViewHolder
    public static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView eventTitle;

        public EventViewHolder(View itemView) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.eventImage);
            eventTitle = itemView.findViewById(R.id.eventTitle);
        }
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = recommendedEvents.get(position);
        holder.eventTitle.setText(event.getTitle());

        // 加載圖片
        Glide.with(holder.eventImage.getContext())
                .load(event.getImageUrl())
                .into(holder.eventImage);

        // 設置點擊監聽器
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 創建跳轉到活動細節頁面的 Intent
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("eventId", event.getId()); // 假設 Event 類別中有 getId() 方法來取得活動的 ID
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return recommendedEvents.size();
    }
}
