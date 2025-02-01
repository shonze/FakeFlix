package com.example.advanced_programing_ex_4;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.advanced_programing_ex_4.Adapters.MoviesListsAdapter;
import com.example.advanced_programing_ex_4.View_Models.MoviesListsViewModel;

public class MainActivity extends AppCompatActivity {

    private MoviesListsViewModel moviesListsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesListsViewModel = new ViewModelProvider(this).get(MoviesListsViewModel.class);

        RecyclerView lstMoviesLists = findViewById(R.id.lstMovies);
        final MoviesListsAdapter moviesListsAdapter = new MoviesListsAdapter(this);
        lstMoviesLists.setAdapter(moviesListsAdapter);
        lstMoviesLists.setLayoutManager(new LinearLayoutManager(this));

        moviesListsViewModel.get().observe(this, moviesLists -> {
            moviesListsAdapter.setMoviesLists(moviesLists);
        });
    }
}