package com.example.advanced_programing_ex_4.Repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.advanced_programing_ex_4.Dao.AppDB;
import com.example.advanced_programing_ex_4.Dao.MoviesListsDao;
import com.example.advanced_programing_ex_4.api.MovieCallback;
import com.example.advanced_programing_ex_4.api.MoviesApi;
import com.example.advanced_programing_ex_4.api.MoviesListsApi;
import com.example.advanced_programing_ex_4.entities.Movie;
import com.example.advanced_programing_ex_4.entities.MoviesList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MoviesListsRepository {
    private MoviesListsDao dao;

    private MoviesListData moviesListData;

    private MoviesListsApi moviesListsApi;

    private MoviesApi moviesApi;

    public MoviesListsRepository(Context context) {
        AppDB db = AppDB.getInstance(context);
        dao = db.moviesListsDao();
        moviesListData = new MoviesListData();
        moviesListsApi = new MoviesListsApi(moviesListData, dao);
        moviesApi = new MoviesApi();
    }

    class MoviesListData extends MutableLiveData<List<MoviesList>> {
        public MoviesListData() {
            super();

            // The movies lists saved in the room database
            List<MoviesList> moviesLists = new LinkedList<>();
            setValue(moviesLists);
        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() -> {
                List<MoviesList> data = dao.get();
                if (data.size() != 0) {
                    for (MoviesList item : data) {
                        List<Movie> movies = new ArrayList<>();
                        for (String movieId : item.getMovieIds()) {
                            // Call the api the wait for the movie to be fetched
                            moviesApi.getMovieById(movieId, new MovieCallback() {
                                @Override
                                public void onSuccess(Movie movie) {
                                    // Do something with the movie
                                    movies.add(movie);
                                }

                                @Override
                                public void onFailure(String errorMessage) {
                                    // Handle the error
                                    System.err.println("Error: " + errorMessage);
                                }
                            });
                        }
                        item.setMovieList(movies);
                    }
                    moviesListData.postValue(data);
                } else {
                    moviesListsApi.get();
                }
            }).start();
        }
    }

    public LiveData<List<MoviesList>> getAll() {
        return moviesListData;
    }
}
