package com.example.advanced_programing_ex_4;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.advanced_programing_ex_4.Adapters.MoviesListsAdapter;
import com.example.advanced_programing_ex_4.Factories.CategoryViewModelFactory;
import com.example.advanced_programing_ex_4.Factories.MoviesViewModelFactory;
import com.example.advanced_programing_ex_4.View_Models.CategoriesViewModel;
import com.example.advanced_programing_ex_4.View_Models.MoviesViewModel;
import com.example.advanced_programing_ex_4.entities.Category;
import com.example.advanced_programing_ex_4.entities.Movie;
import com.example.advanced_programing_ex_4.entities.MoviesList;

import java.util.ArrayList;
import java.util.List;

public class CategoryMoviesActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private NestedScrollView scrollView;
    private CategoriesViewModel categoriesViewModel;
    private MoviesListsAdapter moviesListsAdapter;
    private TextView movieTitleTextView;
    private TextView movieDescription;
    private TextView pageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_search);

        Intent intent = getIntent();  // Get the incoming Intent
        Category category = intent.getParcelableExtra("category");
        List<Movie> movieList = (List<Movie>) intent.getSerializableExtra("movieList");

        category.setMovies(movieList);

        pageTitle = findViewById(R.id.category_search_title);
        pageTitle.setText(category.getName());

        // Initialize the MoviesListsAdapter
        moviesListsAdapter = new MoviesListsAdapter(this);
        RecyclerView lstMoviesLists = findViewById(R.id.movie_lists_recycler_view);
        lstMoviesLists.setAdapter(moviesListsAdapter);
        lstMoviesLists.setLayoutManager(new LinearLayoutManager(this));

        // Initialize other UI elements
        toolbar = findViewById(R.id.toolbar);
        scrollView = findViewById(R.id.nestedScrollView);
        @SuppressLint("WrongViewCast") AppCompatImageButton goBackButton = findViewById(R.id.go_back);
        movieTitleTextView = findViewById(R.id.top_movie_title);
        movieDescription = findViewById(R.id.top_movie_description);

        // Handle scroll transparency for the toolbar
//        if (scrollView != null && toolbar != null) {
//            scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
//                int scrollY = Math.min(scrollView.getScrollY(), 500);
//                float fraction = (float) scrollY / 500;
//                float alpha = 0f + (0.6f - 0f) * fraction;
//                int color = Color.argb((int) (alpha * 255), 255, 255, 255);
//                toolbar.setBackgroundColor(color);
//            });
//        }

        // Navigate to MainActivity on back button click
        if (goBackButton != null) {
            goBackButton.setOnClickListener(v -> {
                finish(); // Closes the current activity and returns to the previous one
            });
        }

        // Directly use the movies in the Category object
        updateMoviesList(category);

        // Display random movie from category
        if (category.getMovies() != null && !category.getMovies().isEmpty()) {
            Movie randomMovie = category.getMovies().get(0); // Example: pick the first movie (you can change this logic)
            movieTitleTextView.setText(randomMovie.getTitle());
            movieDescription.setText(randomMovie.getDescription());
        }
    }

    private void updateMoviesList(Category category) {
        if (category == null || moviesListsAdapter == null) return;

        List<MoviesList> moviesLists = splitMoviesIntoLists(category.getMovies());
        moviesListsAdapter.setMoviesLists(moviesLists);
    }

    private List<MoviesList> splitMoviesIntoLists(List<Movie> movies) {
        List<MoviesList> moviesLists = new ArrayList<>();
        if (movies == null || movies.isEmpty()) return moviesLists;

        List<Movie> batch = new ArrayList<>();
        for (Movie movie : movies) {
            batch.add(movie);
            if (batch.size() == 5) {
                List<String> moviesIds = new ArrayList<>();
                for (Movie item : batch) {
                    moviesIds.add(item.getMovieId());
                }
                MoviesList movieList = new MoviesList("", moviesIds);
                movieList.setMovieList(batch);

                moviesLists.add(movieList);

                batch.clear();
            }
        }
        if (!batch.isEmpty()) {
            List<String> moviesIds = new ArrayList<>();
            for (Movie item : batch) {
                moviesIds.add(item.getMovieId());
            }
            MoviesList movieList = new MoviesList("", moviesIds);
            movieList.setMovieList(batch);

            moviesLists.add(movieList);
        }
        return moviesLists;
    }
}

