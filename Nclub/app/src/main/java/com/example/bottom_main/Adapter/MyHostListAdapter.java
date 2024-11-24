package com.example.bottom_main.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottom_main.R;

import java.util.List;

public class MyHostListAdapter extends RecyclerView.Adapter<MyHostListAdapter.ActivityViewHolder> {

    private Context context;
    private List<String> activityTitles;
    private OnItemClickListener onItemClickListener;

    // 定義點擊事件介面
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Constructor
    public MyHostListAdapter(Context context, List<String> activityTitles, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.activityTitles = activityTitles;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hostlist_item, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        String activityTitle = activityTitles.get(position);
        holder.activityTitleTextView.setText(activityTitle);

        // 點擊事件
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });

        // Switch 開關邏輯（如果需要的話可以開啟）
        // holder.activitySwitch.setOnCheckedChangeListener(null); // 避免 RecyclerView 回收導致的重複觸發
        // holder.activitySwitch.setChecked(false); // 默認設置為關閉
        // holder.activitySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
        //     if (isChecked) {
        //         Toast.makeText(context, activityTitle + " 已開啟", Toast.LENGTH_SHORT).show();
        //     } else {
        //         Toast.makeText(context, activityTitle + " 已關閉", Toast.LENGTH_SHORT).show();
        //     }
        // });
    }

    @Override
    public int getItemCount() {
        return activityTitles.size();
    }

    // ViewHolder Class
    public static class ActivityViewHolder extends RecyclerView.ViewHolder {
        TextView activityTitleTextView;
        Switch activitySwitch;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            activityTitleTextView = itemView.findViewById(R.id.activityTitle);
            activitySwitch = itemView.findViewById(R.id.activitySwitch);
        }
    }
}
