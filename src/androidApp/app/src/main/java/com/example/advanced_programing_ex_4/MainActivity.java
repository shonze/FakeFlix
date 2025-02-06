package com.example.advanced_programing_ex_4;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.advanced_programing_ex_4.Adapters.MoviesListsAdapter;
import com.example.advanced_programing_ex_4.Dao.AppDB;
import com.example.advanced_programing_ex_4.Dao.MoviesListsDao;
import com.example.advanced_programing_ex_4.Factories.MoviesListsViewModelFactory;
import com.example.advanced_programing_ex_4.View_Models.MoviesListsViewModel;
import com.example.advanced_programing_ex_4.entities.Movie;
import com.example.advanced_programing_ex_4.entities.MoviesList;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect to the viewModel
        MoviesListsViewModel moviesListsViewModel = new ViewModelProvider(this, new MoviesListsViewModelFactory(this)).get(MoviesListsViewModel.class);
//        MovieViewModel movieViewModel = new ViewModelProvider(this,new MovieViewModelFactory(this)).get(MovieViewModel.class);

        // Be able to track the changes in the view model
        // And to show it in the screen.
        RecyclerView lstMoviesLists = findViewById(R.id.movie_lists_recycler_view);
        final MoviesListsAdapter moviesListsAdapter = new MoviesListsAdapter(this);
        lstMoviesLists.setAdapter(moviesListsAdapter);
        lstMoviesLists.setLayoutManager(new LinearLayoutManager(this));
        moviesListsViewModel.get().observe(this, moviesListsAdapter::setMoviesLists);

        moviesListsViewModel.get().observe(this, new Observer<List<MoviesList>>() {
            @Override
            public void onChanged(List<MoviesList> moviesLists) {
                Log.d("cheking","changed");
                // Fetch a random movie from a random movieList
                if (!moviesLists.isEmpty()) {
                    MoviesList randomElement = moviesLists.get((int) Math.floor(Math.random() * moviesLists.size()));
                    List<Movie> movieList = randomElement.getMovieList();
                    if (movieList != null && !movieList.isEmpty()) {
                        Movie randomMovie = movieList.get((int) Math.floor(Math.random() * movieList.size()));
                        TextView movieTitleTextView = findViewById(R.id.top_movie_title);
                        TextView movieDescription = findViewById(R.id.top_movie_description);

                        movieTitleTextView.setText(randomMovie.getTitle());
                        movieDescription.setText(randomMovie.getDescription());
                    }
                }
            }
        });
    }
}