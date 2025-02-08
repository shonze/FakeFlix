package com.example.advanced_programing_ex_4.View_Models;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.advanced_programing_ex_4.Repositories.MoviesListsRepository;
import com.example.advanced_programing_ex_4.entities.MoviesList;

import java.util.List;

public class MoviesListsViewModel extends ViewModel {

    private LiveData<List<MoviesList>> moviesLists;
    private MoviesListsRepository repository;

    public MoviesListsViewModel(Context context) {
        repository = new MoviesListsRepository(context);
        this.moviesLists = repository.getAll();
    }

    public LiveData<List<MoviesList>> get() {
        return moviesLists;
    }

    public void reload() { repository.reload(); }
}
