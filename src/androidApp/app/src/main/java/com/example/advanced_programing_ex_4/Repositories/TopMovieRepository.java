package com.example.advanced_programing_ex_4.Repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.advanced_programing_ex_4.Dao.AppDB;
import com.example.advanced_programing_ex_4.Dao.MoviesListsDao;
import com.example.advanced_programing_ex_4.Dao.TopMovieDao;
import com.example.advanced_programing_ex_4.api.MoviesListsApi;
import com.example.advanced_programing_ex_4.entities.Movie;
import com.example.advanced_programing_ex_4.entities.MoviesList;
import com.example.advanced_programing_ex_4.entities.TopMovie;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TopMovieRepository {
    private TopMovieDao dao;

    private TopMovieData topMovieData;

    private MoviesListsApi moviesListsApi;

    public TopMovieRepository(Context context) {
        AppDB db = AppDB.getInstance(context);
        dao = db.topMovieDao();
        topMovieData = new TopMovieData();
    }

    class TopMovieData extends MutableLiveData<TopMovie> {

        public TopMovieData() {
            super();

            setValue(TopMovie);
        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() -> {
                topMovieData.postValue(dao.get());
            }).start();
        }
    }

    public LiveData<TopMovie> getAll() {
        return topMovieData;
    }
}
