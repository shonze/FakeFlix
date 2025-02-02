package com.example.fakeflix;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fakeflix.databinding.ActivityStartingBinding;

public class StartingActivity extends AppCompatActivity {

    private ActivityStartingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user should be remembered
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String rememberMe = sharedPreferences.getString("rememberMe", "false");
        String jwtToken = sharedPreferences.getString("jwtToken", null);

        if ("true".equals(rememberMe) && jwtToken != null) {
            // Redirect to homepage if needed
            //Intent intent = new Intent(this, HomePageActivity.class);
            //startActivity(intent);
            finish(); // Prevent returning to LoginActivity
        }

        setContentView(R.layout.activity_starting);

        binding = ActivityStartingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.getStartedButton.setOnClickListener(view -> {
            // Navigate to RegisterActivity
            Intent intent = new Intent(StartingActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        binding.signInButton.setOnClickListener(view -> {
            // Navigate to LoginActivity
            Intent intent = new Intent(StartingActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}