package com.example.fakeflix;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fakeflix.databinding.ActivityHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private UserDB db;
    private ListView lvPosts;

    private List<String> usernames;
    private List<UserDetails> dbPosts;
    private ArrayAdapter<String> adapter;
    private UserDetailsDao userDetailsDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Room Database
        db = Room.databaseBuilder(getApplicationContext(), UserDB.class, "UserDB")
                .allowMainThreadQueries().build();
        userDetailsDao = db.userDetailsDao();
        handlePosts();
        // Initialize ListView
        Toast.makeText(HomeActivity.this, "loading", Toast.LENGTH_SHORT).show();
        loadPosts(); // Load data from Room
    }

    private void handlePosts() {
        lvPosts = binding.lvPosts;
        usernames = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, usernames);
        loadPosts();
        lvPosts.setAdapter(adapter);
    }
    private void loadPosts() {
        usernames.clear();
        dbPosts = userDetailsDao.index();
        for (UserDetails post : dbPosts){
            usernames.add(post.getId() + "," + post.getUserFullName());
        }
        adapter.notifyDataSetChanged();
    }
}
