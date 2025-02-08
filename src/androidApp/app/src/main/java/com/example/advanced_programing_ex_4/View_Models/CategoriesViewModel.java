package com.example.advanced_programing_ex_4.View_Models;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.advanced_programing_ex_4.Repositories.CategoriesRepository;
import com.example.advanced_programing_ex_4.Repositories.MoviesListsRepository;
import com.example.advanced_programing_ex_4.entities.Category;
import com.example.advanced_programing_ex_4.entities.MoviesList;

import java.util.List;

public class CategoriesViewModel extends ViewModel {
    private LiveData<List<Category>> categoriesList;
    private CategoriesRepository repository;

    private String jwt;

    public CategoriesViewModel(Context context, String jwt) {
        repository = new CategoriesRepository(context,jwt);
        this.categoriesList = repository.getAll();
        this.jwt = jwt;
    }

    public LiveData<List<Category>> get() {
        return categoriesList;
    }

    public void reload() {
        repository.reload();
    }
}
