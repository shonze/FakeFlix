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

    public CategoriesViewModel(Context context) {
        repository = new CategoriesRepository(context);
        this.categoriesList = repository.getAll();
    }

    public LiveData<List<Category>> get() {
        return categoriesList;
    }

    public void reload() {
        repository.reload();
    }
}
