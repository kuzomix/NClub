package com.example.bottom_main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText idEditText;
    private Button saveButton;
    private String userId; // 用戶 ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // 獲取用戶資料
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        userId = intent.getStringExtra("userId"); // 假設你也傳遞了用戶 ID

        // 初始化 UI 元素
        usernameEditText = findViewById(R.id.profile_username);
        emailEditText = findViewById(R.id.profile_email);
        phoneEditText = findViewById(R.id.profile_phone);
        idEditText = findViewById(R.id.profile_ID);
        saveButton = findViewById(R.id.profile_save_button);

        // 設置初始值
        usernameEditText.setText(name);
        emailEditText.setText(email);

        // 保存按鈕的點擊事件
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserProfile();
            }
        });
    }

    private void saveUserProfile() {
        String updatedName = usernameEditText.getText().toString();
        String updatedEmail = emailEditText.getText().toString();
        String updatedPhone = phoneEditText.getText().toString();
        String updatedId = idEditText.getText().toString(); // 使用者ID

        // 更新資料庫
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        databaseReference.child("name").setValue(updatedName);
        databaseReference.child("email").setValue(updatedEmail);
        databaseReference.child("phone").setValue(updatedPhone); // 更新電話號碼
        databaseReference.child("userId").setValue(updatedId) // 更新使用者ID
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditProfileActivity.this, "資料已更新", Toast.LENGTH_SHORT).show();
                        finish(); // 更新後返回
                    } else {
                        Toast.makeText(EditProfileActivity.this, "更新失敗", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
