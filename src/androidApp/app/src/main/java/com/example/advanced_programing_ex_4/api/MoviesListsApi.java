package com.example.advanced_programing_ex_4.api;

import androidx.lifecycle.MutableLiveData;

import com.example.advanced_programing_ex_4.Dao.MoviesListsDao;
import com.example.advanced_programing_ex_4.MyApplication;
import com.example.advanced_programing_ex_4.R;
import com.example.advanced_programing_ex_4.ResnponseEntities.MovieListResponse;
import com.example.advanced_programing_ex_4.entities.MoviesList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesListsApi {
    private MutableLiveData<List<MoviesList>> moviesListData;
    private MoviesListsDao dao;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public MoviesListsApi(MutableLiveData<List<MoviesList>> moviesListData, MoviesListsDao dao) {
        this.moviesListData = moviesListData;
        this.dao = dao;

        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void get() {
        Map<String, String> headers = new HashMap<>();
        headers.put("authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InllYWgiLCJpc0FkbWluIjpmYWxzZSwiaWF0IjoxNzM4NTk2NzQyfQ.d2YFHNmbIZ-OkgoRvvgVG0GtOtfX9mNR2ZPsC3HKHyY");
        headers.put("Content-Type", "application/json");
        Call<Map<String, List<String>>> call = webServiceAPI.getMovies(headers);
        call.enqueue(new Callback<Map<String, List<String>>>() {
            @Override
            public void onResponse(Call<Map<String, List<String>>> call, Response<Map<String, List<String>>> response) {

                if (response.code() == 200) {
//                    new Thread(() -> {
//                        dao.clear();
//                        dao.insertList(response.body());
//                        moviesListData.postValue(dao.get());
//                    }).start();
                }
            }

            @Override
            public void onFailure(Call<Map<String, List<String>>> call, Throwable t) {
            }
        });
    }
}
