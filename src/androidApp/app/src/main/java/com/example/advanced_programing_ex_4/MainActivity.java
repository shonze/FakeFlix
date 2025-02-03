package com.example.advanced_programing_ex_4;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.advanced_programing_ex_4.Adapters.MoviesListsAdapter;
import com.example.advanced_programing_ex_4.Dao.AppDB;
import com.example.advanced_programing_ex_4.Dao.MoviesListsDao;
import com.example.advanced_programing_ex_4.Factories.MovieViewModelFactory;
import com.example.advanced_programing_ex_4.Factories.MoviesListsViewModelFactory;
import com.example.advanced_programing_ex_4.View_Models.MovieViewModel;
import com.example.advanced_programing_ex_4.View_Models.MoviesListsViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect to the viewModel
        MoviesListsViewModel moviesListsViewModel = new ViewModelProvider(this,new MoviesListsViewModelFactory(this)).get(MoviesListsViewModel.class);
        MovieViewModel movieViewModel = new ViewModelProvider(this,new MovieViewModelFactory(this)).get(MovieViewModel.class);

        // Be able to track the changes in the view model
        // And to show it in the screen.
        RecyclerView lstMoviesLists = findViewById(R.id.movielst);
        final MoviesListsAdapter moviesListsAdapter = new MoviesListsAdapter(this, movieViewModel);
        lstMoviesLists.setAdapter(moviesListsAdapter);
        lstMoviesLists.setLayoutManager(new LinearLayoutManager(this));
        moviesListsViewModel.get().observe(this, moviesListsAdapter::setMoviesLists);
    }
}