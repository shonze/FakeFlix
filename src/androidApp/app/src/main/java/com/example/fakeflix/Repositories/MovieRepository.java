package com.example.fakeflix.Repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.fakeflix.DB.MovieDatabase;
import com.example.fakeflix.Dao.MovieDao;
import com.example.fakeflix.api.MovieApiService;
import com.example.fakeflix.entities.Movie;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {
    private final MovieDao movieDao;
    private final MovieApiService movieApiService;
    private final MutableLiveData<List<Movie>> searchResults = new MutableLiveData<>();

    public MovieRepository(Application application) {
        MovieDatabase db = MovieDatabase.getDatabase(application);
        movieDao = db.movieDao();

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.0.16:8080/api/") // Change this to your actual API URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        movieApiService = retrofit.create(MovieApiService.class);

    }

    // 🔍 Fetch movies from the server
    public LiveData<List<Movie>> searchMovies(String token, String query) {
        movieApiService.searchMovies("Bearer " + token, query).enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    searchResults.postValue(response.body());

                    // Optional: Save fetched movies to Room for offline access
                    new Thread(() -> {
                        movieDao.insertMovies(response.body());
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                searchResults.postValue(null);
            }
        });

        return searchResults;
    }
}

