package com.example.bottom_main;

import android.content.Intent;
import android.os.Bundle;
import com.example.bottom_main.databinding.ActivityIntroBinding;

import androidx.appcompat.app.AppCompatActivity;


public class IntroActivity extends AppCompatActivity {
    ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.introBtn.setOnClickListener(view -> startActivity(new Intent(IntroActivity.this, SignupActivity.class)));

    }
}