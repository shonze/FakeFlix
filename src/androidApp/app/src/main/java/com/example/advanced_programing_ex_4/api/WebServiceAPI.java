package com.example.advanced_programing_ex_4.api;

import com.example.advanced_programing_ex_4.entities.MoviesList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WebServiceAPI {
    @GET("movies")
    Call<List<MoviesList>> getMovies();

//    @POST("posts")
//    Call<Void> createPost(@Body Movie movie);
//
//    @DELETE("posts/{id}")
//    Call<Void> deletePost(@Path("id") int id);
 }
