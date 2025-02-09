package com.example.fakeflix;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.fakeflix.Adapters.MovieAdapter;
import com.example.fakeflix.databinding.ActivityLoginBinding;
import com.example.fakeflix.databinding.ActivityWatchMovieBinding;
import com.example.fakeflix.entities.Movie;
import com.example.fakeflix.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WatchMovieActivity extends AppCompatActivity {

    private ImageView movieThumbnail;
    private TextView movieTitle;
    private RecyclerView recyclerView;

    private String movieId;
    private Button watchMovieButton;

    private Button recommendedMoviesButton;
    private TextView movieDescription;
    private TextView movieLength;


    private MovieAdapter movieAdapter;

    private String jwtToken;
    private String theme;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_movie);


        Glide.with(this);

        recommendedMoviesButton = findViewById(R.id.recommendedMoviesButton);

        movieThumbnail = findViewById(R.id.movieThumbnail);
        movieLength = findViewById(R.id.movieLength);

        movieDescription = findViewById(R.id.movieDescription);
        movieTitle = findViewById(R.id.movieTitle);

        movieDescription = findViewById(R.id.movieDescription);

        recyclerView = findViewById(R.id.recommendedMoviesRecyclerView);

        preferences = getSharedPreferences("AppPrefs",Context.MODE_PRIVATE);
        jwtToken = preferences.getString("jwtToken", null);
//        theme = preferences.getString("theme", "dark");

        checkValidation();

        Intent intent = getIntent();
        if (intent != null) {
            movieId = intent.getStringExtra("movieId");
            movieTitle.setText(intent.getStringExtra("movieTitle"));

            String imageUrl = intent.getStringExtra("movieThumbnail");
            Uri imageUri = Uri.parse(imageUrl);

//            getRecommendedMovies(movieId);

            Glide.with(this)
                    .load(imageUri)
                    .into(movieThumbnail);

            movieDescription.setText("Description: " + intent.getStringExtra("movieDescription"));
            movieLength.setText("Length: " + intent.getStringExtra("movieLength") + " minutes");

        }

        watchMovieButton = findViewById(R.id.watchMovieButton);

        watchMovieButton.setOnClickListener(v -> {
            ApiService apiService = RetrofitClient.getApiService();
            Call<ResponseBody> call = apiService.updateUserWatchedMovies("Bearer " + jwtToken, movieId);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Intent intent2 = new Intent(WatchMovieActivity.this, VideoPlayerActivity.class);
                        intent2.putExtra("movieVideo", intent.getStringExtra("movieVideo"));
                        startActivity(intent2);
                    } else {
                        Log.e("WatchMovieActivity", "Response Error: " + response.errorBody());
                        movieAdapter.setMovies(new ArrayList<>()); // Clear list on failure
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("WatchMovieActivity", "API Call Failed: " + t.getMessage());
                }
            });
        });

        recommendedMoviesButton.setOnClickListener(v -> {
            getRecommendedMovies(movieId);
        });
//        new Handler().postDelayed(() -> getRecommendedMovies(movieId), 500);

        movieAdapter = new MovieAdapter(this, movie -> {
            // Handle movie click
            Intent intent2 = new Intent(WatchMovieActivity.this, WatchMovieActivity.class);
//            Intent intent = new Intent(SearchActivity.this, VideoPlayerActivity.class);
            intent2.putExtra("movieId", movie.getMovieId());
            intent2.putExtra("movieTitle", movie.getTitle());
            intent2.putExtra("movieThumbnail", Constants.BASE_URL + "/uploads/" + movie.getThumbnailName());
            intent2.putExtra("movieVideo", Constants.BASE_URL + "/uploads/" + movie.getVideoName());

            intent2.putExtra("movieDescription", movie.getDescription());
            intent2.putExtra("movieLength", movie.getLength());
            intent2.putExtra("movieCategories", movie.getCategories().toArray(new String[0]));
//            intent.putExtra("movieCategories", movie.getCategories());

            startActivity(intent2);
        });

//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(movieAdapter);


//        movieAdapter.setMovies(new ArrayList<>());
//        getRecommendedMovies(movieId);


    }

    private void getRecommendedMovies(String id) {
        if (id.trim().isEmpty()) {
            Toast.makeText(WatchMovieActivity.this, "id is null", Toast.LENGTH_SHORT).show();
            movieAdapter.setMovies(null);
            return;
        }
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<Movie>> call = apiService.getRecommendedMovies("Bearer " + jwtToken, id);
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body();

                    if (movies != null && !movies.isEmpty()) {
                        movieAdapter.setMovies(movies); // Update RecyclerView
                    } else {
                        Toast.makeText(WatchMovieActivity.this, "movies are empty", Toast.LENGTH_SHORT).show();
                        movieAdapter.setMovies(new ArrayList<>()); // Clear list if empty
                    }
                } else {
                    Log.e("WatchMovieActivity", "Response Error: " + response.errorBody());
                    movieAdapter.setMovies(new ArrayList<>()); // Clear list on failure
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e("WatchMovieActivity", "API Call Failed: " + t.getMessage());
            }
        });

    }

    private void checkValidation() {

        if (jwtToken == null) {
            navigateToLogin();
            return;
        }

        ApiService apiService =  RetrofitClient.getApiService();
        Call<ResponseBody> call = apiService.validateToken("Bearer " + jwtToken);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {

                } else {
                    navigateToLogin();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                navigateToLogin();
            }
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
