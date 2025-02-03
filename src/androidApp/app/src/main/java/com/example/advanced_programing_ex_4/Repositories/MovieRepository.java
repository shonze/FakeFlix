package com.example.advanced_programing_ex_4.Repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.advanced_programing_ex_4.Dao.AppDB;
import com.example.advanced_programing_ex_4.Dao.MovieDao;
import com.example.advanced_programing_ex_4.entities.Movie;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import java.util.concurrent.ConcurrentHashMap;

public class MovieRepository {
    private final MovieDao dao;
    private final MoviesData moviesData;
    private final ConcurrentHashMap<String, MovieData> movieLiveDataMap = new ConcurrentHashMap<>();

    public MovieRepository(Context context) {
        AppDB db = AppDB.getInstance(context);
        dao = db.movieDao();
        moviesData = new MoviesData();
    }

    // Fetches a random movie when needed (not stored in LiveData)
    public Movie getRandomMovie() {
        int count = dao.getMoviesCount();
        if (count == 0) return null;

        int randomIndex = new Random().nextInt(count);
        List<String> allMovieIds = dao.getAllMovieIds();
        String randomId = allMovieIds.get(randomIndex);

        return dao.getMovieById(randomId);
    }

    // LiveData for a list of movies
    class MoviesData extends MutableLiveData<List<Movie>> {
        public MoviesData() {
            super();
            setValue(new LinkedList<>()); // Initialize empty list
        }

        @Override
        protected void onActive() {
            super.onActive();
            new Thread(() -> postValue(dao.getAllMovies())).start();
        }
    }

    // LiveData for a single movie by ID
    class MovieData extends MutableLiveData<Movie> {
        public MovieData(String movieId) {
            fetchMovieById(movieId);
        }

        public void fetchMovieById(String movieId) {
            new Thread(() -> postValue(dao.getMovieById(movieId))).start();
        }
    }

    // Public method to expose movies list
    public LiveData<List<Movie>> getAllMovies() {
        return moviesData;
    }

    // Public method to get a movie by ID without losing tracking
    public LiveData<Movie> getMovieById(String movieId) {
        return movieLiveDataMap.computeIfAbsent(movieId, MovieData::new);
    }
}

