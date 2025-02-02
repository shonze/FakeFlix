package com.example.advanced_programing_ex_4.View_Models;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.advanced_programing_ex_4.Repositories.TopMovieRepository;

public class TopMovieViewModel  extends ViewModel {

        private LiveData<TopMovie> movie;
        private TopMovieRepository repository;

        public TopMovieViewModel(Context context) {
            repository = new TopMovieRepository(context);
            this.movie = repository.getAll();
        }

        public LiveData<TopMovie> get() {
            return movie;
        }
}
