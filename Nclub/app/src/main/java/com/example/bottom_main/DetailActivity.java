package com.example.bottom_main;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra(); // Retrieve Intent data
        if (eventId != null) {
            loadEventDetails(); // Load event details using eventId
        } else {
            setVariable(); // Set variables if object is directly passed
        }
    }

    private void getIntentExtra() {
        eventId = getIntent().getStringExtra("eventId");
        if (eventId == null) {
            object = (ItemDomain) getIntent().getSerializableExtra("object");
        }
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
//        binding.durationTxt.setText(object.getDuration());
//        binding.distanceTxt.setText(object.getDistance());
        binding.descriptionTxt.setText(object.getDescription());
        binding.addressTxt.setText(object.getAddress());
        binding.ratingTxt.setText(object.getScore() + "評分");
        binding.ratingBar.setRating((float) object.getScore());

        Glide.with(DetailActivity.this)
                .load(object.getPic())
                .into(binding.pic);

        binding.addToCartBtn.setOnClickListener(view -> {
            Toast.makeText(DetailActivity.this, "已添加到購物車", Toast.LENGTH_SHORT).show();
            // Logic to add the item to the cart can be added here
        });
    }
}
