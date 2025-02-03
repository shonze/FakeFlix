package com.example.advanced_programing_ex_4.api;

import androidx.lifecycle.MutableLiveData;

import com.example.advanced_programing_ex_4.Dao.MoviesListsDao;
import com.example.advanced_programing_ex_4.MyApplication;
import com.example.advanced_programing_ex_4.R;
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
        headers.put("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InllYWgiLCJpc0FkbWluIjpmYWxzZSw\n" +
                "    iaWF0IjoxNzM4NTk2NzQyfQ.d2YFHNmbIZ-OkgoRvvgVG0GtOtfX9mNR2ZPsC3HKHyY");
        headers.put("Content-Type","application/json");
        Call<List<MoviesList>> call = webServiceAPI.getMovies(headers);
        call.enqueue(new Callback<List<MoviesList>>() {
            @Override
            public void onResponse(Call<List<MoviesList>> call, Response<List<MoviesList>> response) {
                if(response.code() == 204) {
                    new Thread(() -> {
                        dao.clear();
                        dao.insertList(response.body());
                        moviesListData.postValue(dao.get());
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<List<MoviesList>> call, Throwable t) {
            }
        });
    }
}
