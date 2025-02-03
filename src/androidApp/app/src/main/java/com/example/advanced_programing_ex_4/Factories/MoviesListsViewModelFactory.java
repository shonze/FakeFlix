package com.example.advanced_programing_ex_4.Factories;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.advanced_programing_ex_4.View_Models.MoviesListsViewModel;

public class MoviesListsViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    public MoviesListsViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MoviesListsViewModel.class)) {
            return (T) new MoviesListsViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
