//package com.example.advanced_programing_ex_4.Factories;
//
//import android.content.Context;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.ViewModel;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.example.advanced_programing_ex_4.View_Models.MoviesListsViewModel;
//
//public class MovieViewModelFactory implements ViewModelProvider.Factory {
//
//    private final Context context;
//
//    public MovieViewModelFactory(Context context) {
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//        if (modelClass.isAssignableFrom(MovieViewModel.class)) {
//            return (T) new MovieViewModel(context);
//        }
//        throw new IllegalArgumentException("Unknown ViewModel class");
//    }
//}
