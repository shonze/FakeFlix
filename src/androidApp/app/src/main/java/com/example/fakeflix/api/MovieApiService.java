package com.example.fakeflix.api;

import com.example.fakeflix.entities.Movie;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MovieApiService {
    @POST("api/tokens/validate")
    Call<ResponseBody> validateToken(@Header("Authorization") String token);

    @GET("api/movies/search/{query}")
    Call<List<Movie>> searchMovies(@Header("Authorization") String token, @Path("query") String query);

}
