package com.example.netflixadmin.data.remote;

import com.example.netflixadmin.data.local.MovieEntity;
import com.example.netflixadmin.data.local.CategoryEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface ApiService {
    @GET("movies")
    Call<List<MovieEntity>> getAllMovies();

    @POST("movies")
    Call<MovieEntity> addMovie(@Body MovieEntity movie);

    @PUT("movies/{id}")
    Call<MovieEntity> updateMovie(@Path("id") String id, @Body MovieEntity movie);

    @DELETE("movies/{id}")
    Call<Void> deleteMovie(@Path("id") String id);

    @GET("categories")
    Call<List<CategoryEntity>> getAllCategories();

    @POST("categories")
    Call<CategoryEntity> addCategory(@Body CategoryEntity category);

    @GET("categories/{id}")
    Call<CategoryEntity> getCategoryById(@Path("id") String id);

    @PATCH("categories/{id}")
    Call<CategoryEntity> updateCategory(@Path("id") String id, @Body CategoryEntity category);

    @DELETE("categories/{id}")
    Call<Void> deleteCategory(@Path("id") String id);
}
