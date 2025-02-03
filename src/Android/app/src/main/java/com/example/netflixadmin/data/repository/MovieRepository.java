package com.example.netflixadmin.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.netflixadmin.data.local.MovieDao;
import com.example.netflixadmin.data.local.MovieDatabase;
import com.example.netflixadmin.data.local.MovieEntity;
import com.example.netflixadmin.data.remote.ApiService;
import com.example.netflixadmin.data.remote.RetrofitClient;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private MovieDao dao;
    private ApiService apiService;
    private MovieListData movieListData;

    public MovieRepository(Application application) {
        // Initialize local database and DAO
        MovieDatabase db = MovieDatabase.getInstance(application);
        dao = db.movieDao();

        // Initialize Retrofit API service
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Initialize MutableLiveData for movies
        movieListData = new MovieListData();
    }

    // Inner class for MutableLiveData
    class MovieListData extends MutableLiveData<List<MovieEntity>> {
        public MovieListData() {
            super();
            setValue(new LinkedList<>()); // Initialize with an empty list
        }

        @Override
        protected void onActive() {
            super.onActive();

            // Fetch data from the local database when the LiveData becomes active
            new Thread(() -> {
                List<MovieEntity> movies = dao.getAllMovies();
                postValue(movies); // Update LiveData with local data

                // Fetch data from the remote API and update the local database
                fetchMoviesFromApi();
            }).start();
        }

        private void fetchMoviesFromApi() {
            apiService.getAllMovies().enqueue(new Callback<List<MovieEntity>>() {
                @Override
                public void onResponse(Call<List<MovieEntity>> call, Response<List<MovieEntity>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Update LiveData with API data
                        postValue(response.body());

                        // Save API data to the local database
                        new Thread(() -> {
                            // Clear existing data in the local database
                            dao.deleteAllMovies();

                            // Insert new data from the API
                            for (MovieEntity movie : response.body()) {
                                dao.insertMovie(movie);
                            }
                        }).start();
                    }
                }

                @Override
                public void onFailure(Call<List<MovieEntity>> call, Throwable t) {
                    // Handle API call failure
                    t.printStackTrace();
                }
            });
        }
    }

    // Method to get all movies as LiveData
    public LiveData<List<MovieEntity>> getAllMovies() {
        return movieListData;
    }

    // Method to add a new movie
    public void addMovie(MovieEntity movie, Callback<MovieEntity> callback) {
        apiService.addMovie(movie).enqueue(callback);
    }

    // Method to update a movie
    public void updateMovie(MovieEntity movie, Callback<MovieEntity> callback) {
        apiService.updateMovie(movie.getId(), movie).enqueue(callback);
    }

    // Method to delete a movie
    public void deleteMovie(String id, Callback<Void> callback) {
        apiService.deleteMovie(id).enqueue(callback);
    }

    // Method to search for a movie by ID
    public LiveData<MovieEntity> getMovieById(String id) {
        MutableLiveData<MovieEntity> movieLiveData = new MutableLiveData<>();
        new Thread(() -> {
            MovieEntity movie = dao.getMovieById(id);
            movieLiveData.postValue(movie);
        }).start();
        return movieLiveData;
    }
}