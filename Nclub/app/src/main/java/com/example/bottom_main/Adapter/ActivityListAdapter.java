package com.example.bottom_main.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottom_main.R;

import java.util.List;

public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.ActivityViewHolder> {

    private Context context;
    private List<String> activityTitles;
    private OnItemClickListener listener;

    // Constructor
    public ActivityListAdapter(Context context, List<String> activityTitles, OnItemClickListener listener) {
        this.context = context;
        this.activityTitles = activityTitles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_list_item, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        String activityTitle = activityTitles.get(position);
        holder.activityTitleTextView.setText(activityTitle);

        // Handle click events
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return activityTitles.size();
    }

    // ViewHolder class
    public static class ActivityViewHolder extends RecyclerView.ViewHolder {
        TextView activityTitleTextView;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            activityTitleTextView = itemView.findViewById(R.id.activityName);
        }
    }

    // Interface for click events
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
