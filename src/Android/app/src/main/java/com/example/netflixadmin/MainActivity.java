package com.example.netflixadmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netflixadmin.R;
import com.example.netflixadmin.ui.activity.AdminActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start the AdminActivity
        Intent intent = new Intent(MainActivity.this, AdminActivity.class);
        startActivity(intent);
        finish(); // Optional: Close MainActivity so user cannot return to it
    }
}
