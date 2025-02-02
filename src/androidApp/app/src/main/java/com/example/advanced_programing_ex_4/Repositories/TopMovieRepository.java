package com.example.advanced_programing_ex_4.Repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.advanced_programing_ex_4.Dao.AppDB;
import com.example.advanced_programing_ex_4.Dao.MovieDao;
import com.example.advanced_programing_ex_4.api.MoviesListsApi;
import com.example.advanced_programing_ex_4.entities.Movie;

import java.util.List;

public class TopMovieRepository {
    private MovieDao dao;

    private TopMovieData topMovieData;

    public TopMovieRepository(Context context) {
        AppDB db = AppDB.getInstance(context);
        dao = db.movieDao();
        topMovieData = new TopMovieData();
    }

    public Movie getRandomMovie(MovieDao movieDao) {
        int count = movieDao.getMoviesCount();
        if (count == 0) return null;  // No movies in DB

        int randomIndex = new Random().nextInt(count);  // Generate random index
        List<String> allMovieIds = movieDao.getAllMovieIds();  // Fetch all IDs
        String randomId = allMovieIds.get(randomIndex);

        return movieDao.getMovieById(randomId);
    }

    class TopMovieData extends MutableLiveData<Movie> {

        public TopMovieData() {
            super();

            setValue(getRandomMovie(dao));
        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() -> {
                TopMovieData.postValue(dao.get());
            }).start();
        }
    }

    public LiveData<Movie> getAll() {
        return topMovieData;
    }
}
