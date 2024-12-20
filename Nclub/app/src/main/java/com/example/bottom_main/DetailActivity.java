package com.example.bottom_main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bottom_main.Domain.ItemDomain;
import com.example.bottom_main.databinding.ActivityDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private ItemDomain object;
    private String eventId;
    private boolean isFollowing = false; // 預設為未關注
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra(); // 獲取 Intent 資料
        if (eventId != null) {
            loadEventDetails(); // 使用 eventId 加載活動詳細內容
        } else {
            setVariable(); // 如果直接傳遞了物件，則設置變數
        }

    }

    private void getIntentExtra() {
        eventId = getIntent().getStringExtra("eventId");
        if (eventId == null) {
            object = (ItemDomain) getIntent().getSerializableExtra("object");
        }
        userId = getIntent().getStringExtra("userId");
    }

    private void loadEventDetails() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Items").child(eventId);
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    object = dataSnapshot.getValue(ItemDomain.class);
                    if (object != null) {
                        setVariable();
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
        if (object == null) return;
        binding.titleTxt.setText(object.getTitle());
        binding.priceTxt.setText(String.valueOf(object.getPrice()));
        binding.backBtn.setOnClickListener(view -> finish());
        binding.bedTxt.setText(String.valueOf(object.getBed()));
//        binding.durationTxt.setText(object.getDuration()); // 確保這行不被註釋
//        binding.distanceTxt.setText(object.getDistance()); // 確保這行不被註釋
        binding.descriptionTxt.setText(object.getDescription());
        binding.addressTxt.setText(object.getAddress());
        binding.ratingTxt.setText(object.getScore() + "評分");
        binding.ratingBar.setRating((float) object.getScore());
        binding.durationTxt.setText(object.getStartDateTour());
        binding.durationTxt2.setText(object.getEndDateTour());
        binding.distanceTxt.setText(object.getStartTimeTour());
        binding.distanceTxt2.setText(object.getEndTimeTour());

        if (userId == null) userId = object.getUserId();
        if (eventId == null) eventId =  object.getItemId();

        Glide.with(DetailActivity.this)
                .load(object.getPic())
                .into(binding.pic);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

        userRef.child("follow").child(eventId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    isFollowing = true; // 用戶已關注
                    binding.follow.setImageResource(R.drawable.follow_icon); // 設置為紅色圖標
                } else {
                    isFollowing = false; // 用戶未關注
                    binding.follow.setImageResource(R.drawable.fav_icon); // 設置為默認圖標
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DetailActivity.this, "讀取數據失敗：" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // 關注按鈕的點擊事件
        binding.follow.setOnClickListener(view -> {
            toggleFollow(eventId);
        });

//        binding.addToCartBtn.setOnClickListener(view -> {
//            Toast.makeText(DetailActivity.this, "準備參加此活動", Toast.LENGTH_SHORT).show();
//            Log.e("Debug", "DetailActivity- userId: " + userId);
//            Log.e("Debug", "DetailActivity- itemId: " + userId);


            String chatroomId = "chatroomId_"+eventId.substring(7);
            DatabaseReference chatroomRef = FirebaseDatabase.getInstance().getReference("chatrooms").child(chatroomId).child("members");

//            // 將用戶 ID 添加到 members 中
//            chatroomRef.child(userId).setValue(false).addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    Toast.makeText(DetailActivity.this, "已報名活動並審核中", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(DetailActivity.this, "報名失敗：" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
            // 檢查 userId 是否存在於聊天室成員中
            chatroomRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        // userId 不存在，啟用按鈕
                        binding.addToCartBtn.setEnabled(true);
                        binding.addToCartBtn.setOnClickListener(view -> {
                            Toast.makeText(DetailActivity.this, "報名中，請稍後!", Toast.LENGTH_SHORT).show();
                            Log.e("Debug", "DetailActivity- userId: " + userId);
                            Log.e("Debug", "DetailActivity- itemId: " + eventId);

                            // 將用戶 ID 添加到 members 中
                            chatroomRef.child(userId).setValue(false).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(DetailActivity.this, "已報名活動並審核中", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(DetailActivity.this, "報名失敗：" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
                    } else {
                        // userId 存在，禁用按鈕
                        binding.addToCartBtn.setEnabled(false);
                        // userId 存在，檢查報名狀態
                        Boolean isRegistered = dataSnapshot.getValue(Boolean.class);
                        if (isRegistered != null) {
                            if (isRegistered) {
                                Toast.makeText(DetailActivity.this, "您已加入活動，請勿重複報名活動", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DetailActivity.this, "您已加入活動(審核中)，請勿重複報名活動", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(DetailActivity.this, "讀取數據失敗：" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
//        });
    }
    private void toggleFollow(String itemId) {

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

        if (isFollowing) {
            // 如果已關注，取消關注
            userRef.child("follow").child(itemId).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    isFollowing = false; // 更新狀態
                    binding.follow.setImageResource(R.drawable.fav_icon); // 設置為默認圖標
                    Toast.makeText(DetailActivity.this, "已取消關注", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailActivity.this, "取消關注失敗：" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // 如果未關注，添加關注
            userRef.child("follow").child(itemId).setValue(true).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    isFollowing = true; // 更新狀態
                    binding.follow.setImageResource(R.drawable.follow_icon); // 設置為紅色圖標
                    Toast.makeText(DetailActivity.this, "關注成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailActivity.this, "關注失敗：" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
