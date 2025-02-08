package com.example.advanced_programing_ex_4.api;

import androidx.lifecycle.MutableLiveData;

import com.example.advanced_programing_ex_4.Dao.MoviesListsDao;
import com.example.advanced_programing_ex_4.MyApplication;
import com.example.fakeflix.R;
import com.example.advanced_programing_ex_4.Repositories.MoviesListsRepository;
import com.example.advanced_programing_ex_4.entities.Movie;
import com.example.advanced_programing_ex_4.entities.MoviesList;
import com.example.fakeflix.utils.Constants;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesListsApi {
    // So we can use some of its special functunalities
    private MoviesListsRepository.MoviesListData moviesListData;
    private MoviesListsDao dao;
    private MoviesApi moviesApi;

    private String jwt;

    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public MoviesListsApi(MoviesListsRepository.MoviesListData moviesListData, MoviesListsDao dao,String jwt) {
        this.moviesListData = moviesListData;
        this.dao = dao;
        this.moviesApi = new MoviesApi(jwt);
        this.jwt = jwt;

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL + "/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void get(final MovieListCallback callback) {
        Map<String, String> headers = new HashMap<>();
        headers.put("authorization", "Bearer " + jwt); //eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InllYWgiLCJpc0FkbWluIjpmYWxzZSwiaWF0IjoxNzM4NTk2NzQyfQ.d2YFHNmbIZ-OkgoRvvgVG0GtOtfX9mNR2ZPsC3HKHyY");
        headers.put("Content-Type", "application/json");
        Call<Map<String, List<String>>> call = webServiceAPI.getMovies(headers);
        call.enqueue(new Callback<Map<String, List<String>>>() {
            @Override
            public void onResponse(Call<Map<String, List<String>>> call, Response<Map<String, List<String>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    // Handle the case where the response is not successful or the body is null
                    callback.onFailure(null);
                }
            }

            @Override
            public void onFailure(Call<Map<String, List<String>>> call, Throwable t) {
                callback.onFailure(null);
            }
        });
    }
}
