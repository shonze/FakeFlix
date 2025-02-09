package com.example.advanced_programing_ex_4;

import static com.example.advanced_programing_ex_4.MyApplication.context;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.advanced_programing_ex_4.Adapters.MoviesListsAdapter;
import com.example.advanced_programing_ex_4.Factories.CategoryViewModelFactory;
import com.example.advanced_programing_ex_4.Factories.MoviesViewModelFactory;
import com.example.advanced_programing_ex_4.View_Models.CategoriesViewModel;
import com.example.advanced_programing_ex_4.View_Models.MoviesViewModel;
import com.example.advanced_programing_ex_4.entities.Category;
import com.example.advanced_programing_ex_4.entities.Movie;
import com.example.advanced_programing_ex_4.entities.MoviesList;
import com.example.fakeflix.R;
import com.example.fakeflix.SearchActivity;
import com.example.fakeflix.VideoPlayerActivity;
import com.example.fakeflix.WatchMovieActivity;
import com.example.fakeflix.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class CategoryMoviesActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private NestedScrollView scrollView;
    private CategoriesViewModel categoriesViewModel;
    private MoviesListsAdapter moviesListsAdapter;
    private TextView topMovieTitleTextView;
    private TextView topMovieDescription;
    private ImageView topMovieThumbnail;
    private TextView pageTitle;

    private SharedPreferences preferences;

    private String jwtToken;
    private Movie randomMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_search);

        @SuppressLint("WrongViewCast") AppCompatImageButton searchButton = findViewById(R.id.search_button);
        if (searchButton != null) {
            searchButton.setOnClickListener(v -> {
                Intent intent = new Intent(CategoryMoviesActivity.this, SearchActivity.class);

                startActivity(intent);
            });
        }

        Intent intent = getIntent();  // Get the incoming Intent
        Category category = intent.getParcelableExtra("category");
        List<Movie> movieList = (List<Movie>) intent.getSerializableExtra("movieList");

        // Get the user token
        preferences = getSharedPreferences("AppPrefs", getApplicationContext().MODE_PRIVATE);
        jwtToken = preferences.getString("jwtToken", null);


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
        topMovieTitleTextView = findViewById(R.id.top_movie_title);
        topMovieDescription = findViewById(R.id.top_movie_description);
        topMovieThumbnail = findViewById(R.id.top_movie_image);
        Button topMoviePlayButton = findViewById(R.id.top_movie_play_button);
        Button topMovieDescriptionButton = findViewById(R.id.top_movie_info_button);

        // Navigate to MainActivity on back button click
        if (goBackButton != null) {
            goBackButton.setOnClickListener(v -> {
                finish(); // Closes the current activity and returns to the previous one
            });
        }

        if (topMoviePlayButton != null) {
            topMoviePlayButton.setOnClickListener(v -> {
                if(randomMovie != null) {
                    Intent intent2 = new Intent(CategoryMoviesActivity.this, VideoPlayerActivity.class);

                    intent2.putExtra("movieId", randomMovie.getMovieId());
                    intent2.putExtra("movieTitle", randomMovie.getTitle());
                    intent2.putExtra("movieThumbnail", Constants.BASE_URL + "/uploads/" + randomMovie.getThumbnailName());
                    intent2.putExtra("movieVideo", Constants.BASE_URL + "/uploads/" + randomMovie.getVideoName());
                    intent2.putExtra("movieDescription", randomMovie.getDescription());
                    intent2.putExtra("movieLength", randomMovie.getLength());
                    intent2.putExtra("movieCategories", randomMovie.getCategories().toArray(new String[0]));

                    startActivity(intent2);
                }
            });
        }

        if (topMovieDescriptionButton != null) {
            topMovieDescriptionButton.setOnClickListener(v -> {
                if(randomMovie != null) {
                    Intent intent3 = new Intent(CategoryMoviesActivity.this, WatchMovieActivity.class);

                    intent3.putExtra("movieId", randomMovie.getMovieId());
                    intent3.putExtra("movieTitle", randomMovie.getTitle());
                    intent3.putExtra("movieThumbnail", Constants.BASE_URL + "/uploads/" + randomMovie.getThumbnailName());
                    intent3.putExtra("movieVideo", Constants.BASE_URL + "/uploads/" + randomMovie.getVideoName());
                    intent3.putExtra("movieDescription", randomMovie.getDescription());
                    intent3.putExtra("movieLength", randomMovie.getLength());
                    intent3.putExtra("movieCategories", randomMovie.getCategories().toArray(new String[0]));

                    startActivity(intent3);
                }
            });
        }

        // Directly use the movies in the Category object
        updateMoviesList(category);

        // Display random movie from category
        if (category.getMovies() != null && !category.getMovies().isEmpty()) {
            randomMovie = category.getMovies().get(0);

            if (randomMovie != null) {
                if (topMovieTitleTextView != null) {
                    topMovieTitleTextView.setText(randomMovie.getTitle());
                }
                if (topMovieDescription != null) {
                    topMovieDescription.setText(randomMovie.getDescription());
                }

                String thumbnailUrl = Constants.BASE_URL + "/uploads/" + randomMovie.getThumbnailName();

                // Set image using Glide
                Glide.with(topMovieThumbnail.getContext())
                        .load(Uri.parse(thumbnailUrl)) // URL of the image
                        .placeholder(R.drawable.sample_thumbnail_background) // Default image while loading
                        .error(R.drawable.sample_thumbnail_background) // If failed to load
                        .into(topMovieThumbnail); // ImageView reference
            }
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

