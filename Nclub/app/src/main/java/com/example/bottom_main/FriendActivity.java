package com.example.bottom_main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bottom_main.Adapter.FriendsListAdapter;

import java.util.ArrayList;
import java.util.List;

public class FriendActivity extends AppCompatActivity {
    private EditText etFriendEmail;
    private Button btnAddFriend, btnBack;
    private RecyclerView recyclerFriends; // 用於顯示好友列表

    private FriendsListAdapter friendsListAdapter;
    private List<String> friendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend); // 確保這裡的布局文件名稱正確

        // 初始化 UI 元素
        etFriendEmail = findViewById(R.id.friend_email);
        btnAddFriend = findViewById(R.id.add_friend_button);
        btnBack = findViewById(R.id.friendback); // 獲取返回按鈕的引用
        recyclerFriends = findViewById(R.id.friend_recycler_view); // 初始化 RecyclerView

        // 初始化好友列表和適配器
        friendsList = new ArrayList<>();
        friendsListAdapter = new FriendsListAdapter(friendsList);

        // 設定 RecyclerView
        recyclerFriends.setLayoutManager(new LinearLayoutManager(this));
        recyclerFriends.setAdapter(friendsListAdapter);

        // 設定點擊事件
        btnBack.setOnClickListener(view -> finish());
        btnAddFriend.setOnClickListener(view -> addFriend());
    }

    private void addFriend() {
        String friendEmail = etFriendEmail.getText().toString().trim();

        if (TextUtils.isEmpty(friendEmail)) {
            Toast.makeText(FriendActivity.this, "請輸入好友的使用者名稱", Toast.LENGTH_SHORT).show();
            return;
        }

        // 將好友電子郵件添加到列表並通知適配器
        friendsList.add(friendEmail);
        friendsListAdapter.notifyItemInserted(friendsList.size() - 1);
        etFriendEmail.setText(""); // 清空輸入框
        Toast.makeText(FriendActivity.this, "好友已添加", Toast.LENGTH_SHORT).show();
    }
}
