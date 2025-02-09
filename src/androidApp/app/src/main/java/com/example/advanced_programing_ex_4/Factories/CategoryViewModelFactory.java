package com.example.advanced_programing_ex_4.Factories;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.advanced_programing_ex_4.View_Models.CategoriesViewModel;
import com.example.advanced_programing_ex_4.View_Models.MoviesViewModel;

public class CategoryViewModelFactory implements ViewModelProvider.Factory{
    private final Context context;
    private final String jwt;

    public CategoryViewModelFactory(Context context, String jwt) {
        this.context = context;
        this.jwt = jwt;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CategoriesViewModel.class)) {
            return (T) new CategoriesViewModel(context,jwt);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
