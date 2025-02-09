package com.example.fakeflix;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fakeflix.Adapters.MovieAdapter;
import com.example.fakeflix.entities.Movie;
import com.example.fakeflix.utils.Constants;

import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private EditText searchInput;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private ProgressBar progressBar;
    private TextView noResultsText;
    private boolean isAdmin = false;
    private boolean isLogged = false;
    private String jwtToken;
    private String theme;
    private SharedPreferences preferences;
    private final Handler handler = new Handler();
    private final long debounceDelay = 100; // 100ms debounce delay
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchInput = findViewById(R.id.searchInput);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        noResultsText = findViewById(R.id.noResultsText);

        preferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        jwtToken = preferences.getString("jwtToken", null);
        theme = preferences.getString("theme", "dark");

        checkValidation();

        movieAdapter = new MovieAdapter(this, movie -> {
            // Handle movie click
            Intent intent = new Intent(SearchActivity.this, WatchMovieActivity.class);
//            Intent intent = new Intent(SearchActivity.this, VideoPlayerActivity.class);


            intent.putExtra("movieId", movie.getMovieId());
            intent.putExtra("movieTitle", movie.getTitle());
            intent.putExtra("movieThumbnail", Constants.BASE_URL + "/uploads/" + movie.getThumbnailName());
            intent.putExtra("movieVideo", Constants.BASE_URL + "/uploads/" + movie.getVideoName());

            intent.putExtra("movieDescription", movie.getDescription());
            intent.putExtra("movieLength", movie.getLength());
            intent.putExtra("movieCategories", movie.getCategories().toArray(new String[0]));
//            intent.putExtra("movieCategories", movie.getCategories());

            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(movieAdapter);

        searchInput.addTextChangedListener(new TextWatcher() {
            private Runnable searchRunnable = () -> searchMovies(searchInput.getText().toString());

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(searchRunnable);
                handler.postDelayed(searchRunnable, debounceDelay);
            }

            @Override
            public void afterTextChanged(Editable s) {}
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

    private void searchMovies(String query) {
        if (query.trim().isEmpty()) {
            movieAdapter.setMovies(null);
            noResultsText.setVisibility(View.GONE);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<Movie>> call = apiService.searchMovies("Bearer " + jwtToken, query);

        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                progressBar.setVisibility(View.GONE); // Hide loading indicator

                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body();

                    if (movies != null && !movies.isEmpty()) {
                        movieAdapter.setMovies(movies); // Update RecyclerView
                        noResultsText.setVisibility(View.GONE); // Hide "No Results" text
                    } else {
                        movieAdapter.setMovies(new ArrayList<>()); // Clear list if empty
                        noResultsText.setVisibility(View.VISIBLE); // Show "No Results"
                    }
                } else {
                    Log.e("SearchActivity", "Response Error: " + response.errorBody());
                    movieAdapter.setMovies(new ArrayList<>()); // Clear list on failure
                    noResultsText.setVisibility(View.VISIBLE); // Show "No Results"
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("SearchActivity", "API Call Failed: " + t.getMessage());
                noResultsText.setVisibility(View.VISIBLE);
            }
        });

    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
