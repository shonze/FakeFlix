package com.example.fakeflix;

import com.example.fakeflix.entities.Movie;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    @POST("users")  // This appends "/api/users" to the base URL
    Call<AuthResponse> registerUser(@Body User user);

    @POST("users/check")
    Call<ResponseBody> checkUser(@Body User user);

    @Multipart
    @POST("file")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part image);

    @POST("tokens")
    Call<AuthResponse> loginUser(@Body Login login);

    @GET("tokens/user")
    Call<ResponseBody> getUserDetails(@Header("Authorization") String authToken);

    @POST("tokens/validate")
    Call<ResponseBody> validateToken(@Header("Authorization") String token);

    @GET("movies/search/{query}")
    Call<List<Movie>> searchMovies(@Header("Authorization") String token, @Path("query") String query);
}

