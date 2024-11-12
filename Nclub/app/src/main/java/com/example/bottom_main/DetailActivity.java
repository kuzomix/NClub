package com.example.bottom_main;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;   //11.11new

import com.bumptech.glide.Glide;
import com.example.bottom_main.Domain.ItemDomain;
import com.example.bottom_main.databinding.ActivityDetailBinding;
import com.google.firebase.database.DataSnapshot;      //11.11new
import com.google.firebase.database.DatabaseError;     //11.11new
import com.google.firebase.database.DatabaseReference; //11.11new
import com.google.firebase.database.FirebaseDatabase;  //11.11new
import com.google.firebase.database.ValueEventListener;//11.11new

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private ItemDomain object;
    private String eventId; // 11.11new 新增變量存儲 eventId

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        eventId = getIntent().getStringExtra("eventId");
        if (eventId != null) {  //11.11new if.else
            loadEventDetails(); // 使用 eventId 加載活動詳細內容
        } else {
            setVariable();
        }
    }

    //11.11new
    private void getIntentExtra() {
        // 嘗試從 Intent 中獲取 eventId
        eventId = getIntent().getStringExtra("eventId");
        if (eventId == null) {
            // 如果沒有 eventId，則嘗試獲取序列化的 object
            object = (ItemDomain) getIntent().getSerializableExtra("object");
        }
    }

    //11.11new
    private void loadEventDetails() {
        // 根據 eventId 從 Firebase 加載活動詳細資訊
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Items").child(eventId);
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    object = dataSnapshot.getValue(ItemDomain.class); // 將資料庫內容映射到 ItemDomain 類
                    if (object != null) {
                        setVariable(); // 更新視圖
                    }
                } else {
                    Toast.makeText(DetailActivity.this, "無法找到活動詳細內容", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DetailActivity.this, "讀取數據失敗：" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setVariable() {
        if (object == null) return; // 11.11檢查 object 是否為 null
        binding.titleTxt.setText(object.getTitle());
        binding.priceTxt.setText("" + object.getPrice());
        binding.backBtn.setOnClickListener(view -> finish());
        binding.bedTxt.setText("" + object.getBed());
        binding.durationTxt.setText(object.getDuration());
        binding.distanceTxt.setText(object.getDistance());
        binding.descriptionTxt.setText(object.getDescription());
        binding.addressTxt.setText(object.getAddress());
        binding.ratingTxt.setText(object.getScore() + "評分");
        binding.ratingBar.setRating((float) object.getScore());

        Glide.with(DetailActivity.this)
                .load(object.getPic())
                .into(binding.pic);

        binding.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(DetailActivity.this,)
            }
        });
    }
}

//    private void getIntentExtra() {
//        object= (ItemDomain) getIntent().getSerializableExtra("object");
//    }
//}