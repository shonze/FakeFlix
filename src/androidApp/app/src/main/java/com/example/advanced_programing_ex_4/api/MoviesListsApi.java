package com.example.advanced_programing_ex_4.api;

import androidx.lifecycle.MutableLiveData;

import com.example.advanced_programing_ex_4.MyApplication;
import com.example.advanced_programing_ex_4.R;
import com.example.advanced_programing_ex_4.entities.MoviesList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesListsApi {
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public MoviesListsApi() {
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void get(MutableLiveData<List<MoviesList>> moviesListData) {
        Call<List<MoviesList>> call = webServiceAPI.getMovies();
        call.enqueue(new Callback<List<MoviesList>>() {
            @Override
            public void onResponse(Call<List<MoviesList>> call, Response<List<MoviesList>> response) {

                moviesListData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<MoviesList>> call, Throwable t) {
            }
        });
    }
}
