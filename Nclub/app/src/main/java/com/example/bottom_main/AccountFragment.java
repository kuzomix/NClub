package com.example.bottom_main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bottom_main.databinding.FragmentAccountBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    private String userId;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ImageView accountBack = view.findViewById(R.id.account_back);

        // 獲取傳遞的參數
        Bundle bundle = getArguments();
        if (bundle != null) {
            String userName = bundle.getString("username");
            userId = bundle.getString("userId");
            Log.e("AccountFragment", "userName: " + userName);
            Log.e("AccountFragment", "userId: " + userId);

            TextView usernameTextView1 = view.findViewById(R.id.username_text_view);
            usernameTextView1.setText(userName);  // 設定用戶名
        }

        // 返回主頁面
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
        initActivitylistFunctionality(); // 11.15初始化活動列表功能
        initHostActivityFunctionality(); // 11.16初始化我主辦的活動功能
        initLogoutFunctionality(); // 初始化登出功能

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setFabVisibility(View.GONE); // 隱藏 FAB
        }
    }

    // 個人資料功能的初始化
    private void initEditProfileActivityFunctionality() {
        ImageView set = binding.accountset; // 假設你在布局中有一個 ID 為 accountset 的 ImageView
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 獲取用戶名
//                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
//                String userName = sharedPreferences.getString("username", null); // 假設用戶名是用戶 ID
                Log.e("AccountFragment", "userId: " + userId);

                if (userId != null) {
                    // 從資料庫獲取用戶資料
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // 假設資料庫中有 name 和 email 欄位
                                String name = dataSnapshot.child("name").getValue(String.class);
                                String email = dataSnapshot.child("email").getValue(String.class);

                                // 將資料傳遞到 EditProfileActivity
                                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                                intent.putExtra("name", name);
                                intent.putExtra("email", email);
                                startActivity(intent); // 跳轉至 EditProfileActivity
                            } else {
                                Toast.makeText(getActivity(), "用戶資料不存在", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getActivity(), "資料獲取失敗: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "用戶未登入", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //初始化活動列表功能
    private void initActivitylistFunctionality(){
        ImageView Actlist = binding.imageView5;
        Actlist.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activitylist.class);
                startActivity(intent); // 跳轉至 Activitylist
            }
        });
    }

    //初始化活動列表功能
    private void initHostActivityFunctionality(){
        ImageView Actlist = binding.imageView11;
        Actlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activitylist.class);
                startActivity(intent); // 跳轉至 Activitylist
            }
        });
    }

    // 登出功能的初始化
    private void initLogoutFunctionality() {
        TextView logout = binding.textView6; // 假設你在布局中有一個 ID 為 textView6 的 TextView
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 儲存帳號和密碼
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", ""); // 清除帳號
                editor.putString("password", ""); // 清除密碼
                editor.apply();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent); // 跳轉至 LoginActivity
            }
        });
    }
}
