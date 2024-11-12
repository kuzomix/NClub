package com.example.bottom_main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bottom_main.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    String username, email, userId; // 將 username, email 和 userId 宣告為成員變數

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 獲取傳遞過來的資料
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        email = intent.getStringExtra("email");
        userId = intent.getStringExtra("userId");

        // 傳遞資料給 HomeFragment
        Bundle homeBundle = new Bundle();
        homeBundle.putString("username", username);
        homeBundle.putString("email", email);
        homeBundle.putString("userId", userId);
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(homeBundle);

        // 替換為 HomeFragment
        replaceFragment(homeFragment);

        // 設置底部導航視圖
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.home:
                    // 傳遞資料給 HomeFragment
                    selectedFragment = new HomeFragment();
                    Bundle homeArgs = new Bundle();
                    homeArgs.putString("username", username);
                    homeArgs.putString("email", email);
                    homeArgs.putString("userId", userId);
                    selectedFragment.setArguments(homeArgs);
                    break;

                case R.id.calendar:
                    selectedFragment = new CalendarFragment();
                    break;

                case R.id.chat:
                    // 傳遞資料給 ChatFragment
                    Bundle chatArgs = new Bundle();
                    chatArgs.putString("username", username);
                    chatArgs.putString("email", email);
                    chatArgs.putString("userId", userId);
                    selectedFragment = new ChatFragment(chatArgs);
                    break;

                case R.id.account:
                    // 傳遞資料給 AccountFragment
                    selectedFragment = new AccountFragment();
                    Bundle accountArgs = new Bundle();
                    accountArgs.putString("username", username);
                    accountArgs.putString("email", email);
                    accountArgs.putString("userId", userId);
                    selectedFragment.setArguments(accountArgs);
                    break;
            }

            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
            }

            return true;
        });

        binding.fab.setOnClickListener(view -> showBottomDialog());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void showBottomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout videoLayout = dialog.findViewById(R.id.layoutVideo);
        LinearLayout shortsLayout = dialog.findViewById(R.id.layoutShorts);
        LinearLayout liveLayout = dialog.findViewById(R.id.layoutLive);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        videoLayout.setOnClickListener(v -> {
            dialog.dismiss();
            // 創建 CallFragment 實例
            CallFragment callFragment = new CallFragment();
            // 傳遞 username
            Bundle callArgs = new Bundle();
            callArgs.putString("username", username);
            callArgs.putString("userId", userId);
            callFragment.setArguments(callArgs);

            replaceFragment(callFragment);
            Toast.makeText(MainActivity.this, "創建召集版", Toast.LENGTH_SHORT).show();
        });

        shortsLayout.setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(MainActivity.this, "Create a short is Clicked", Toast.LENGTH_SHORT).show();
        });

        liveLayout.setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(MainActivity.this, "Go live is Clicked", Toast.LENGTH_SHORT).show();
        });

        cancelButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}
