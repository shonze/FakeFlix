package com.example.advanced_programing_ex_4.api;

import androidx.lifecycle.MutableLiveData;

import com.example.advanced_programing_ex_4.Dao.MovieDao;
import com.example.advanced_programing_ex_4.MyApplication;
import com.example.advanced_programing_ex_4.R;
import com.example.advanced_programing_ex_4.api.WebServiceAPI;
import com.example.advanced_programing_ex_4.entities.Movie;
import com.example.advanced_programing_ex_4.entities.MoviesList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesApi {
    private WebServiceAPI webServiceAPI;

    public MoviesApi() {
        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    // Fetch the movie from the API if not found in local database
    public Movie getMovieById(String movieId){
        Map<String, String> headers = new HashMap<>();
        headers.put("authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InllYWgiLCJpc0FkbWluIjpmYWxzZSwiaWF0IjoxNzM4NTk2NzQyfQ.d2YFHNmbIZ-OkgoRvvgVG0GtOtfX9mNR2ZPsC3HKHyY");
        headers.put("Content-Type", "application/json");
        final Movie[] returnedMovie = new Movie[1];
        Call<Movie> call = webServiceAPI.getMovieById(movieId, headers);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    returnedMovie[0] = response.body();
                } else {
                    // Handle response failure
                    System.err.println("Failed to fetch movie from API: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                // Handle failure
                System.err.println("API call failed: " + t.getMessage());
            }
        });
        return returnedMovie[0];
    }
}

