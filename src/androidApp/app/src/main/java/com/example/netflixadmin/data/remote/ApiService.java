package com.example.netflixadmin.data.remote;

import com.example.netflixadmin.data.local.MovieEntity;
import com.example.netflixadmin.data.local.CategoryEntity;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.PATCH;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    @GET("movies")
    Call<List<MovieEntity>> getAllMovies(@Header("Authorization") String token);

    @POST("movies")
    Call<MovieEntity> addMovie(@Header("Authorization") String token, @Body MovieEntity movie);

    @PUT("movies/{id}")
    Call<MovieEntity> updateMovie(@Header("Authorization") String token,@Path("id") String id, @Body MovieEntity movie);

    @DELETE("movies/{id}")
    Call<Void> deleteMovie(@Header("Authorization") String token, @Path("id") String id);

    @GET("movies/{id}")
    Call<MovieEntity> getMovieById(@Header("Authorization") String token, @Path("id") String id);

    @Multipart
    @POST("file") // Update the endpoint based on your backend
    Call<ResponseBody> uploadImage(@Header("Authorization") String token, @Part MultipartBody.Part image);

    @Multipart
    @POST("file") // Update the endpoint based on your backend
    Call<ResponseBody> uploadVideo(@Header("Authorization") String token, @Part MultipartBody.Part video);

    @GET("categories")
    Call<List<CategoryEntity>> getAllCategories(@Header("Authorization") String token);

    @POST("categories")
    Call<CategoryEntity> addCategory(@Header("Authorization") String token, @Body CategoryEntity category);

    @GET("categories/{id}")
    Call<CategoryEntity> getCategoryById(@Header("Authorization") String token, @Path("id") String id);

    @PATCH("categories/{id}")
    Call<CategoryEntity> updateCategory(@Header("Authorization") String token, @Path("id") String id, @Body CategoryEntity category);

    @DELETE("categories/{id}")
    Call<Void> deleteCategory(@Header("Authorization") String token, @Path("id") String id);
}
