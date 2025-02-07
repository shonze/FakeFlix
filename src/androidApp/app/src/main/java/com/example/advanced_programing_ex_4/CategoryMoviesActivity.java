package com.example.advanced_programing_ex_4;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private MoviesViewModel moviesViewModel;
    private MoviesListsAdapter moviesListsAdapter;
    private TextView movieTitleTextView;
    private TextView movieDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Category category = (Category) getIntent().getSerializableExtra("category");

        if (category == null) {
            finish(); // Exit if no category is provided
            return;
        }

        categoriesViewModel = new ViewModelProvider(this, new CategoryViewModelFactory(this)).get(CategoriesViewModel.class);
        moviesViewModel = new ViewModelProvider(this, new MoviesViewModelFactory(this)).get(MoviesViewModel.class);

        RecyclerView lstMoviesLists = findViewById(R.id.movie_lists_recycler_view);
        moviesListsAdapter = new MoviesListsAdapter(this);
        lstMoviesLists.setAdapter(moviesListsAdapter);
        lstMoviesLists.setLayoutManager(new LinearLayoutManager(this));

        toolbar = findViewById(R.id.toolbar);
        scrollView = findViewById(R.id.nestedScrollView);
        AppCompatImageButton goBackButton = findViewById(R.id.go_back);
        movieTitleTextView = findViewById(R.id.top_movie_title);
        movieDescription = findViewById(R.id.top_movie_description);

        // Update movies list if category is not null
        updateMoviesList(category);
        observeCategoryChanges(category);

        // Handle scroll transparency
        if (scrollView != null && toolbar != null) {
            scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
                int scrollY = Math.min(scrollView.getScrollY(), 500);
                float fraction = (float) scrollY / 500;
                float alpha = 0f + (0.6f - 0f) * fraction;
                int color = Color.argb((int) (alpha * 255), 255, 255, 255);
                toolbar.setBackgroundColor(color);
            });
        }

        // Navigate to MainActivity
        if (goBackButton != null) {
            goBackButton.setOnClickListener(v -> {
                finish(); // Closes the current activity and returns to the previous one
            });
        }
    }

    private void observeCategoryChanges(Category category) {
        if (categoriesViewModel == null) return;

        categoriesViewModel.get().observe(this, categories -> {
            if (categories == null || category == null) return;

            for (Category updatedCategory : categories) {
                if (updatedCategory.getCategoryId().equals(category.getCategoryId()) &&
                        !updatedCategory.getMoviesIds().equals(category.getMoviesIds())) {

                    category.setMoviesIds(updatedCategory.getMoviesIds());
                    updateMoviesList(category);
                }
            }

            // Display random movie from updated list
            if (moviesViewModel != null) {
                Movie randomMovie = moviesViewModel.getRandomMovie(category.getMoviesIds());
                if (randomMovie != null && movieTitleTextView != null && movieDescription != null) {
                    movieTitleTextView.setText(randomMovie.getTitle());
                    movieDescription.setText(randomMovie.getDescription());
                }
            }
        });
    }

    private void updateMoviesList(Category category) {
        if (category == null || moviesViewModel == null || moviesListsAdapter == null) return;

        List<MoviesList> moviesLists = splitMoviesIntoLists(category.getMoviesIds());
        for (MoviesList moviesList : moviesLists) {
            moviesViewModel.getMoviesByIds(moviesList.getMovieIds()).observe(this, movies -> {
                if (movies != null && !movies.isEmpty()) {
                    moviesList.setMovieList(movies);
                    moviesListsAdapter.setMoviesLists(new ArrayList<>(moviesLists));
                }
            });
        }
    }

    private List<MoviesList> splitMoviesIntoLists(List<String> movieIds) {
        List<MoviesList> moviesLists = new ArrayList<>();
        if (movieIds == null || movieIds.isEmpty()) return moviesLists;

        List<String> batch = new ArrayList<>();
        for (String movieId : movieIds) {
            if (movieId != null) {
                batch.add(movieId);
                if (batch.size() == 5) {
                    moviesLists.add(new MoviesList("", new ArrayList<>(batch)));
                    batch.clear();
                }
            }
        }
        if (!batch.isEmpty()) {
            moviesLists.add(new MoviesList("", batch));
        }
        return moviesLists;
    }
}
