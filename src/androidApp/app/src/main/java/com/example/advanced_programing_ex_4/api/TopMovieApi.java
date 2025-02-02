package com.example.advanced_programing_ex_4.api;

import androidx.lifecycle.MutableLiveData;

import com.example.advanced_programing_ex_4.Dao.MoviesListsDao;
import com.example.advanced_programing_ex_4.Dao.TopMovieDao;
import com.example.advanced_programing_ex_4.MyApplication;
import com.example.advanced_programing_ex_4.R;
import com.example.advanced_programing_ex_4.entities.MoviesList;
import com.example.advanced_programing_ex_4.entities.TopMovie;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TopMovieApi {
    private MutableLiveData<TopMovie> topMovieData;
    private TopMovieDao dao;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public TopMovieApi(MutableLiveData<TopMovie> topMovieData, TopMovieDao dao) {
        this.topMovieData = topMovieData;
        this.dao = dao;

        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void get() {
        Call<List<MoviesList>> call = webServiceAPI.getMovies();
        call.enqueue(new Callback<List<MoviesList>>() {
            @Override
            public void onResponse(Call<List<MoviesList>> call, Response<List<MoviesList>> response) {

                new Thread(() -> {
                    dao.clear();
                    dao.insertList(response.body());
                    postListData.postValue(dao.get());
                }).start();
            }

            @Override
            public void onFailure(Call<List<MoviesList>> call, Throwable t) {
            }
        });
    }
}
