package com.example.fakeflix.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.fakeflix.data.local.CategoryEntity;
import com.example.fakeflix.data.repository.CategoryRepository;

import java.util.List;

import retrofit2.Callback;

public class CategoryViewModel extends AndroidViewModel {
    private final CategoryRepository repository;
    public MutableLiveData<List<CategoryEntity>> categoriesLiveData = new MutableLiveData<>();

    public CategoryViewModel(String jwtToken, Application application) {
        super(application);
        repository = new CategoryRepository(jwtToken, application);
    }

    public void fetchCategories() {
        repository.getAllCategories().observeForever(categoriesLiveData::setValue);
    }

    public void addCategory(CategoryEntity category, Callback<CategoryEntity> callback) {
        repository.addCategory(category, callback);
    }

    public void updateCategory(String id, CategoryEntity category, Callback<CategoryEntity> callback) {
        repository.updateCategory(id, category, callback);
    }

    public void deleteCategory(String id, Callback<Void> callback) {
        repository.deleteCategory(id, callback);
    }
}