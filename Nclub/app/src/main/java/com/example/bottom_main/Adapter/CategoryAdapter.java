package com.example.bottom_main.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bottom_main.CategoryDetailActivity;
import com.example.bottom_main.Domain.Category;
import com.example.bottom_main.R;
import com.example.bottom_main.databinding.ViewholderCategoryBinding;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private final List<Category> items;
    private int selectedPosition = -1;
    private int lastSelectedPosition = -1;
    private final Context context;

    public CategoryAdapter(List<Category> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewholderCategoryBinding binding = ViewholderCategoryBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category item = items.get(position);
        holder.binding.title.setText(item.getName());

        Glide.with(holder.itemView.getContext())
                .load(item.getImagePath())
                .into(holder.binding.pic);

        holder.binding.getRoot().setOnClickListener(view -> {
            // 如果是同一個位置被點擊兩次，則跳轉到詳細頁面
            if (selectedPosition == position) {
                // 點擊兩次同一個項目，跳轉到詳細頁面
                Intent intent = new Intent(context, CategoryDetailActivity.class);
                intent.putExtra("categoryName", item.getName());  // 傳遞分類名稱
                context.startActivity(intent);  // 啟動詳細頁面
            } else {
                // 如果是第一次選擇該項目，則更新選中的項目
                lastSelectedPosition = selectedPosition;
                selectedPosition = position;
                notifyItemChanged(lastSelectedPosition);
                notifyItemChanged(selectedPosition);
            }
        });

        if (selectedPosition == position) {
            holder.binding.pic.setBackground(null);
            holder.binding.mainLayout.setBackgroundResource(R.drawable.white_corner_bg);
            holder.binding.title.setVisibility(View.VISIBLE);
        } else {
            holder.binding.pic.setBackgroundResource(R.drawable.gray_bg);
            holder.binding.mainLayout.setBackground(null);
            holder.binding.title.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ViewholderCategoryBinding binding;

        public ViewHolder(ViewholderCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
