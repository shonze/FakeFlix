package com.example.advanced_programing_ex_4.Factories;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.advanced_programing_ex_4.View_Models.MoviesListsViewModel;
import com.example.advanced_programing_ex_4.View_Models.MoviesViewModel;

public class MoviesViewModelFactory implements ViewModelProvider.Factory{
    private final Context context;

    public MoviesViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MoviesViewModel.class)) {
            return (T) new MoviesViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
