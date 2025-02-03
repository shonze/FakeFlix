package com.example.advanced_programing_ex_4.View_Models;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.advanced_programing_ex_4.Repositories.MovieRepository;
import com.example.advanced_programing_ex_4.entities.Movie;

import java.util.List;

public class MovieViewModel extends ViewModel {

    private LiveData<List<Movie>> movies;
    private MovieRepository repository;

    public MovieViewModel(Context context) {
        repository = new MovieRepository(context);
        this.movies = repository.getAllMovies();
    }

    public LiveData<List<Movie>> get() {
        return movies;
    }

    public LiveData<Movie> getMovieById(String movieId) {
        return repository.getMovieById(movieId);
    }
}
