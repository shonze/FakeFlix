package com.example.advanced_programing_ex_4.Repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.advanced_programing_ex_4.api.MoviesListsApi;
import com.example.advanced_programing_ex_4.entities.Movie;
import com.example.advanced_programing_ex_4.entities.MoviesList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MoviesListsRepository {
    //private PostDao dao;
    private MoviesListData moviesListData;

    private MoviesListsApi moviesListsApi = new MoviesListsApi();

    public MoviesListsRepository() {
//        LocalDatabase db = LocalDatabase.getInstance();
//        dao = db.postDao();
        moviesListData = new MoviesListData();
    }

    class MoviesListData extends MutableLiveData<List<MoviesList>> {

        public MoviesListData() {
            super();

            List<MoviesList> moviesLists = new LinkedList<>();
            List<Movie> movies = new ArrayList<>();
            movies.add(new Movie("Kifas"));
            movies.add(new Movie("Mia"));
            moviesLists.add(new MoviesList("Wala", movies));
            moviesLists.add(new MoviesList("Babala", movies));

            setValue(moviesLists);
        }

        @Override
        protected void onActive() {
            super.onActive();

//            new Thread(() -> {
//                moviesListData.postValue(dao.get());
//            }).start();
            moviesListsApi.get(this);
        }
    }

    public LiveData<List<MoviesList>> getAll() {
        return moviesListData;
    }
}
