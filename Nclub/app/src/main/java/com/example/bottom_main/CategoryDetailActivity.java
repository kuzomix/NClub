package com.example.bottom_main;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import com.example.bottom_main.Domain.ItemDomain;

public class CategoryDetailActivity extends AppCompatActivity {

    private RecyclerView eventsRecyclerView;  // RecyclerView，用來顯示活動列表
    private CategoryEventsAdapter adapter;  // 改為 CategoryEventsAdapter
    private List<ItemDomain> events = new ArrayList<>();  // 用來存儲指定分類下的所有活動

    private String categoryId;  // 用來保存傳遞過來的分類 ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);  // 設置正確的佈局

        // 初始化 RecyclerView 並設置適配器
        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        adapter = new CategoryEventsAdapter(events, this);  // 使用 CategoryEventsAdapter

        // 設置 GridLayoutManager 顯示為兩列
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        eventsRecyclerView.setLayoutManager(gridLayoutManager);

        // 設置適配器給 RecyclerView
        eventsRecyclerView.setAdapter(adapter);

        // 獲取從 Intent 傳遞過來的分類 ID
        categoryId = getIntent().getStringExtra("categoryId");

        // 如果分類 ID 有值，則加載該分類下的所有活動
        if (categoryId != null) {
            loadCategoryEvents(categoryId);  // 根據 categoryId 加載該分類的活動
        } else {
            // 如果分類 ID 無效，顯示提示
            Toast.makeText(this, "無效的分類 ID", Toast.LENGTH_SHORT).show();
        }
    }

    // 從 Firebase 根據分類 ID 加載活動資料
    private void loadCategoryEvents(String categoryId) {
        // 引用 Firebase 中的 "Items" 節點
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference("Items");

        // 根據 category 篩選活動，這裡是查詢所有屬於指定 categoryId 的活動
        itemsRef.orderByChild("category").equalTo(categoryId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        events.clear();  // 清空舊的活動資料

                        // 遍歷查詢結果並將活動加入列表
                        for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                            try {
                                // 直接將 DataSnapshot 轉換為 ItemDomain
                                ItemDomain item = eventSnapshot.getValue(ItemDomain.class);

                                // 確保數據完整再加入列表
                                if (item != null) {
                                    events.add(item);
                                    Log.d("Firebase", "已載入活動: " + item.getTitle());
                                } else {
                                    Log.w("Firebase", "資料為空，略過此活動");
                                }
                            } catch (Exception e) {
                                Log.e("Firebase", "資料解析失敗: " + e.getMessage());
                            }
                        }

                        // 通知適配器資料已經更新，刷新 RecyclerView
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // 如果 Firebase 加載資料失敗，顯示錯誤提示
                        Log.e("Firebase", "加載活動資料失敗", databaseError.toException());
                        Toast.makeText(CategoryDetailActivity.this, "加載活動資料失敗", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
