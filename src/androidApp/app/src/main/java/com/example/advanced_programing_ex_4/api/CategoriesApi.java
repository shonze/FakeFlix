package com.example.advanced_programing_ex_4.api;

import android.content.SharedPreferences;

import com.example.advanced_programing_ex_4.Dao.CategoriesDao;
import com.example.advanced_programing_ex_4.MyApplication;
import com.example.fakeflix.R;
import com.example.advanced_programing_ex_4.Repositories.CategoriesRepository;
import com.example.advanced_programing_ex_4.Repositories.MoviesListsRepository;
import com.example.advanced_programing_ex_4.entities.Category;
import com.example.advanced_programing_ex_4.entities.Movie;
import com.example.fakeflix.utils.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoriesApi {
    private final WebServiceAPI webServiceAPI;
    private final CategoriesDao dao;
    private CategoriesRepository.CategoriesData categoriesData;

    private String jwt;

    public CategoriesApi(CategoriesDao dao,CategoriesRepository.CategoriesData categoriesData,String jwt) {
        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL + "/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.webServiceAPI = retrofit.create(WebServiceAPI.class);

        this.categoriesData = categoriesData;
        this.dao = dao;
        this.jwt = jwt;
    }

    // Fetch the movie from the API if not found in local database
    public void get(){
        Map<String, String> headers = new HashMap<>();
        headers.put("authorization", "Bearer " + jwt); //eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InllYWgiLCJpc0FkbWluIjpmYWxzZSwiaWF0IjoxNzM4NTk2NzQyfQ.d2YFHNmbIZ-OkgoRvvgVG0GtOtfX9mNR2ZPsC3HKHyY");
        headers.put("Content-Type", "application/json");

        Call<List<Category>> call = webServiceAPI.getCategories(headers);
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    new Thread(() -> {
                        dao.clear();
                        dao.insertList(response.body());

                        categoriesData.fetchMovieDetails(response.body(),false);
                    }).start();
                }
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
            }
        });
    }
}
