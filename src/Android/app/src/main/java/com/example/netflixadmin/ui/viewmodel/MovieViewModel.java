package com.example.netflixadmin.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.netflixadmin.data.local.MovieEntity;
import com.example.netflixadmin.data.repository.MovieRepository;

import com.example.netflixadmin.data.local.CategoryEntity;
import com.example.netflixadmin.data.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieViewModel extends AndroidViewModel {
    private final MovieRepository repository;
    private final CategoryRepository categoryRepository;
    public MutableLiveData<List<MovieEntity>> moviesLiveData = new MutableLiveData<>();

    public MovieViewModel(Application application) {
        super(application);
        repository = new MovieRepository(application);
        categoryRepository = new CategoryRepository(application);
    }

    public  LiveData<List<CategoryEntity>> getCategories(){
        return categoryRepository.getAllCategories();
    }

    public void fetchMovies() {
        repository.getAllMovies().observeForever(moviesLiveData::setValue);
    }

    public void addMovie(MovieEntity movie, Callback<MovieEntity> callback) {
        repository.addMovie(movie, callback);
    }

    public void updateMovie(MovieEntity movie, Callback<MovieEntity> callback) {
        repository.updateMovie(movie, callback);
    }

    public void deleteMovie(String id, Callback<Void> callback) {
        repository.deleteMovie(id, callback);
    }

    public LiveData<MovieEntity> getMovieById(String id) {
        return repository.getMovieById(id);
    }


}