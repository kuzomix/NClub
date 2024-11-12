package com.example.bottom_main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bottom_main.databinding.FragmentAccountBinding;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;


public class AccountFragment extends Fragment {

    private TextView usernameTextView;
    private FragmentAccountBinding binding;
    private FirebaseDatabase database;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ImageView accountBack = view.findViewById(R.id.account_back);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String name1 = bundle.getString("name");
            TextView usernameTextView1 = view.findViewById(R.id.username_text_view);
            usernameTextView1.setText(name1);  // 在這裡設定 name
        }

        // Return to home screen on back button click
        accountBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "回到主頁面", Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, new HomeFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        initEditProfileActivityFunctionality(); // 初始化個人資料功能
        initFriendActivityFunctionality(); // 初始化好友列表功能
        initLogoutFunctionality(); // 初始化登出功能

        return view;
    }
    // 個人資料功能的初始化
    private void initEditProfileActivityFunctionality() {
        // 設定通知按鈕的點擊事件
        ImageView set = binding.accountset; // 假設你在布局中有一個 ID 為 imageView 的 ImageView
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent); // 跳轉至 EditProfileActivity
            }
        });
    }
    // 好友列表功能的初始化
    private void initFriendActivityFunctionality() {
        // 設定通知按鈕的點擊事件
        ImageView friend = binding.imageView5; // 假設你在布局中有一個 ID 為 imageView 的 ImageView
        friend .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FriendActivity.class);
                startActivity(intent); // 跳轉至 FriendActivity
            }
        });
    }
    // 好友列表功能的初始化
    private void initLogoutFunctionality() {
        // 設定通知按鈕的點擊事件
        TextView logout = binding.textView6; // 假設你在布局中有一個 ID 為 textView6 的 TextView
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent); // 跳轉至 LoginActivity
            }
        });
    }
}
