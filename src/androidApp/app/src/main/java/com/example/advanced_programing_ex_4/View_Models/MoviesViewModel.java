package com.example.advanced_programing_ex_4.View_Models;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.advanced_programing_ex_4.Repositories.CategoriesRepository;
import com.example.advanced_programing_ex_4.Repositories.MovieRepository;
import com.example.advanced_programing_ex_4.entities.Category;
import com.example.advanced_programing_ex_4.entities.Movie;

import java.util.ArrayList;
import java.util.List;

public class MoviesViewModel extends ViewModel {
    private LiveData<List<Movie>> movieList;
    private MovieRepository repository;

    private String jwt;

    public MoviesViewModel(Context context,String jwt) {
        repository = new MovieRepository(context,jwt);
        this.movieList = repository.getAll();
        this.jwt = jwt;
    }

    public LiveData<List<Movie>> get() {
        return movieList;
    }

    public Movie getRandomMovie(List<String> moviesIds){
        return repository.getRandomMovie(moviesIds);
    }

    public LiveData<List<Movie>> getMoviesByIds(List<String> movieIds) {
        if (movieIds == null || movieIds.isEmpty()) {
            return new MutableLiveData<>(new ArrayList<>());
        }
        return repository.getMoviesByIds(movieIds);
    }
}
