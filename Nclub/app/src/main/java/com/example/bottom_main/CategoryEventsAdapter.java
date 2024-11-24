package com.example.bottom_main;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bottom_main.Domain.ItemDomain;

import java.util.List;

public class CategoryEventsAdapter extends RecyclerView.Adapter<CategoryEventsAdapter.ViewHolder> {

    private List<ItemDomain> items;
    private Context context;

    public CategoryEventsAdapter(List<ItemDomain> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemDomain item = items.get(position);

        holder.titleTextView.setText(item.getTitle());

        Glide.with(context)
                .load(item.getPic())
                .placeholder(new ColorDrawable(ContextCompat.getColor(context, R.color.gray))) // Use the defined color
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.event_title);
            imageView = itemView.findViewById(R.id.event_image);
        }
    }
}
