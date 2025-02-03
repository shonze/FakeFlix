package com.example.fakeflix;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.fakeflix.databinding.ActivityRegisterBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ArrayAdapter;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import com.example.fakeflix.databinding.ActivityHomeBinding;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    private UserDB db;
    private UserDetails userDetails;
    UserDetailsDao userDetailsDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        db = Room.databaseBuilder(getApplicationContext(), UserDB.class, "UserDB")
                .allowMainThreadQueries().build();
        userDetailsDao = db.userDetailsDao();

    }

}