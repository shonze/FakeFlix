package com.example.advanced_programing_ex_4.Repositories;

import android.content.Context;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.advanced_programing_ex_4.Dao.AppDB;
import com.example.advanced_programing_ex_4.Dao.CategoriesDao;
import com.example.advanced_programing_ex_4.Dao.MovieDao;
import com.example.advanced_programing_ex_4.R;
import com.example.advanced_programing_ex_4.api.CategoriesApi;
import com.example.advanced_programing_ex_4.api.MovieCallback;
import com.example.advanced_programing_ex_4.api.MoviesApi;
import com.example.advanced_programing_ex_4.entities.Category;
import com.example.advanced_programing_ex_4.entities.Movie;
import com.example.advanced_programing_ex_4.entities.MoviesList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MovieRepository {
    private MovieDao dao;

    private MoviesApi moviesApi;

    private MoviesData moviesData;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public MovieRepository(Context context) {
        AppDB db = AppDB.getInstance(context);
        dao = db.movieDao();
        moviesData = new MoviesData();
        moviesApi = new MoviesApi();
    }

    public class MoviesData extends MutableLiveData<List<Movie>> {
        public MoviesData() {
            super();

            List<Movie> moviesLists = new LinkedList<>();
            setValue(moviesLists);
        }

        @Override
        protected void onActive() {
            super.onActive();

            executorService.execute(() -> {
                List<Movie> data = dao.getAllMovies();
                postValue(data);
            });
        }
    }

    public LiveData<List<Movie>> getAll() {
        return moviesData;
    }

    public Movie getRandomMovie(List<String> moviesIds) {
        final Movie[] randomMovie = {null};

        CountDownLatch latch = new CountDownLatch(1);
        executorService.execute(() -> {
            if (!moviesIds.isEmpty()) {
                String randomId = moviesIds.get((int) Math.floor(Math.random() * moviesIds.size()));
                randomMovie[0] = dao.getMovieById(randomId);
                if (randomMovie[0] == null) {
                    moviesApi.getMovieById(randomId, new MovieCallback() {
                        @Override
                        public void onSuccess(Movie movie) {
                            randomMovie[0] = movie;
                            executorService.execute(() -> dao.insertMovie(movie));
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                        }
                    });
                }
            }
            latch.countDown();
        });
        try {
            latch.await(5, TimeUnit.SECONDS); // Prevent infinite wait
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return randomMovie[0];
    }

    public LiveData<List<Movie>> getMoviesByIds(List<String> movieIds) {
        MutableLiveData<List<Movie>> liveData = new MutableLiveData<>();

        executorService.execute(() -> {
            List<Movie> movies = dao.getMoviesByIdsSync(movieIds); // Fetch from DB

            List<String> missingIds = movieIds.stream()
                    .filter(id -> movies.stream().noneMatch(movie -> movie.getMovieId().equals(id)))
                    .collect(Collectors.toList());

            if (!missingIds.isEmpty()) {
                List<Movie> fetchedMovies = Collections.synchronizedList(new ArrayList<>());
                CountDownLatch latch = new CountDownLatch(missingIds.size());

                for (String movieId : missingIds) {
                    moviesApi.getMovieById(movieId, new MovieCallback() {
                        @Override
                        public void onSuccess(Movie movie) {
                            fetchedMovies.add(movie);
                            dao.insertMovies(Collections.singletonList(movie)); // Store in DB
                            latch.countDown();
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            System.err.println("Error fetching movie " + movieId + ": " + errorMessage);
                            latch.countDown();
                        }
                    });
                }

                try {
                    latch.await(); // Wait for all API calls to complete
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                movies.addAll(fetchedMovies);
            }

            liveData.postValue(movies);
        });

        return liveData;
    }
}
