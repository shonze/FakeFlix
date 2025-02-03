package com.example.advanced_programing_ex_4.api;

import androidx.lifecycle.MutableLiveData;

import com.example.advanced_programing_ex_4.Dao.MoviesListsDao;
import com.example.advanced_programing_ex_4.MyApplication;
import com.example.advanced_programing_ex_4.R;
import com.example.advanced_programing_ex_4.entities.Movie;
import com.example.advanced_programing_ex_4.entities.MoviesList;

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
    private MutableLiveData<List<MoviesList>> moviesListData;
    private MoviesListsDao dao;

    private MoviesApi moviesApi;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public MoviesListsApi(MutableLiveData<List<MoviesList>> moviesListData, MoviesListsDao dao) {
        this.moviesListData = moviesListData;
        this.dao = dao;
        this.moviesApi = new MoviesApi();

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
                if (response.isSuccessful() && response.body() != null) {
                    new Thread(() -> {
                        Map<String, List<String>> responseBody = response.body();
                        List<MoviesList> movieLists = new ArrayList<>();

                        for (Map.Entry<String, List<String>> entry : responseBody.entrySet()) {
                            String key = entry.getKey();
                            List<String> moviesIds = entry.getValue();
                            if (moviesIds.size() != 0) {
                                List<Movie> movies = new ArrayList<>();
                                for(String movieId:moviesIds){
                                    movies.add(moviesApi.getMovieById(movieId));
                                }
                                // Assuming MovieList has a constructor that accepts a key and a list of movies
                                MoviesList movieList = new MoviesList(key, movies);
                                movieLists.add(movieList);

                                // For debugging purposes
                                System.out.println("Key = " + key + ", Movies = " + movies);
                            }
                        }

                        // Clear the DAO and insert the new list
                        dao.clear();
                        dao.insertList(movieLists);
                        moviesListData.postValue(dao.get());
                    }).start();
                } else {
                    // Handle the case where the response is not successful or the body is null
                    System.err.println("Response was not successful or body was null. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Map<String, List<String>>> call, Throwable t) {

            }
        });
    }
}
