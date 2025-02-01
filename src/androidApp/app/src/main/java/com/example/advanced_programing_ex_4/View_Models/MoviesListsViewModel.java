package com.example.advanced_programing_ex_4.View_Models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.advanced_programing_ex_4.Repositories.MoviesListsRepository;
import com.example.advanced_programing_ex_4.entities.MoviesList;

import java.util.List;

public class MoviesListsViewModel extends ViewModel {

    private LiveData<List<MoviesList>> moviesLists;
    private MoviesListsRepository repository;

    public MoviesListsViewModel() {
        repository = new MoviesListsRepository();
        this.moviesLists = repository.getAll();
    }

    public LiveData<List<MoviesList>> get() {
        return moviesLists;
    }

//    public void add(MoviesList moviesList) { repository.add(moviesList); }
//
//    public void delete(MoviesList moviesList) { repository.delete(moviesList); }
//
//    public void reload() { repository.reload(); }
}
