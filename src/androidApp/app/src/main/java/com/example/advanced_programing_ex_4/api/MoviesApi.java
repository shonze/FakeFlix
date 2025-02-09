package com.example.advanced_programing_ex_4.api;

import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

import com.example.advanced_programing_ex_4.Dao.MovieDao;
import com.example.advanced_programing_ex_4.MyApplication;
import com.example.fakeflix.R;
import com.example.advanced_programing_ex_4.api.WebServiceAPI;
import com.example.advanced_programing_ex_4.entities.Movie;
import com.example.advanced_programing_ex_4.entities.MoviesList;
import com.example.fakeflix.utils.Constants;

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
    private String jwt;

    public MoviesApi(String jwt) {
        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL + "/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.webServiceAPI = retrofit.create(WebServiceAPI.class);
        this.jwt = jwt;
    }

    // Fetch the movie from the API if not found in local database
    public void getMovieById(String movieId,final MovieCallback callback){
        Map<String, String> headers = new HashMap<>();
        headers.put("authorization", "Bearer " + jwt); //eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InllYWgiLCJpc0FkbWluIjpmYWxzZSwiaWF0IjoxNzM4NTk2NzQyfQ.d2YFHNmbIZ-OkgoRvvgVG0GtOtfX9mNR2ZPsC3HKHyY");
        headers.put("Content-Type", "application/json");

        Call<Movie> call = webServiceAPI.getMovieById(movieId, headers);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    // Handle response failure
                    callback.onFailure(null);
                }
            }
            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                // Handle failure
                callback.onFailure(null);
            }
        });
    }
}

