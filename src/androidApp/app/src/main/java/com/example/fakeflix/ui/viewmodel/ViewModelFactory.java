package com.example.fakeflix.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final String jwtToken;
    private Application app;

    public ViewModelFactory(String jwtToken, Application app) {
        this.jwtToken = jwtToken;
        this.app= app;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CategoryViewModel.class)) {
            return (T) new CategoryViewModel(jwtToken, app);
        } else if (modelClass.isAssignableFrom(MovieViewModel.class)) {
            return (T) new MovieViewModel(jwtToken, app);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

