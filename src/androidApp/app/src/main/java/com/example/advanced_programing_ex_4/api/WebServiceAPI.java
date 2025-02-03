package com.example.advanced_programing_ex_4.api;

import androidx.room.Query;

import com.example.advanced_programing_ex_4.entities.Movie;
import com.example.advanced_programing_ex_4.entities.MoviesList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;

public interface WebServiceAPI {
    @GET("movies")
    Call<Map<String, List<String>>> getMovies(@HeaderMap Map<String, String> headers);

    @GET("movies/{movieId}")
    Call<Movie> getMovieById(@Path("movieId") String movieId, @HeaderMap Map<String, String> headers);

//    @POST("posts")
//    Call<Void> createPost(@Body Movie movie);
//
//    @DELETE("posts/{id}")
//    Call<Void> deletePost(@Path("id") int id);
}
