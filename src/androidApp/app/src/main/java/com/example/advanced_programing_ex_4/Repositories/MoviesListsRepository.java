package com.example.advanced_programing_ex_4.Repositories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.advanced_programing_ex_4.Dao.AppDB;
import com.example.advanced_programing_ex_4.Dao.MovieDao;
import com.example.advanced_programing_ex_4.Dao.MoviesListsDao;
import com.example.advanced_programing_ex_4.api.MovieCallback;
import com.example.advanced_programing_ex_4.api.MovieListCallback;
import com.example.advanced_programing_ex_4.api.MoviesApi;
import com.example.advanced_programing_ex_4.api.MoviesListsApi;
import com.example.advanced_programing_ex_4.entities.Movie;
import com.example.advanced_programing_ex_4.entities.MoviesList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MoviesListsRepository {
    private MoviesListsDao dao;

    private MovieDao movieDao;

    private MoviesListData moviesListData;

    private MoviesListsApi moviesListsApi;

    private MoviesApi moviesApi;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private String jwt;

    public MoviesListsRepository(Context context,String jwt) {
        AppDB db = AppDB.getInstance(context);
        dao = db.moviesListsDao();
        movieDao = db.movieDao();
        moviesListData = new MoviesListData();
        moviesListsApi = new MoviesListsApi(moviesListData, dao, jwt);
        moviesApi = new MoviesApi(jwt);
        this.jwt = jwt;
    }

    public class MoviesListData extends MutableLiveData<List<MoviesList>> {
        public MoviesListData() {
            super();

            // The movies lists saved in the room database
            List<MoviesList> moviesLists = new LinkedList<>();
            setValue(moviesLists);
        }

        @Override
        protected void onActive() {
            super.onActive();

            executorService.execute(() -> {
                List<MoviesList> data = dao.get();

                if (data.isEmpty()) {
                    // The api will enter the movies to the dao
                    fetchMoviesFromApi();
                    return;
                }
                fetchMovieDetails(data);
            });
        }

        /**
         * Fetches movies from the API when no data is available in the database.
         */
        public void fetchMoviesFromApi() {
            moviesListsApi.get(new MovieListCallback() {
                @Override
                public void onSuccess(Map<String, List<String>> responseBody) {
                    new Thread(() -> {
                        List<MoviesList> movieLists = new ArrayList<>();

                        // Transform the map into moviesLists
                        for (Map.Entry<String, List<String>> entry : responseBody.entrySet()) {
                            String key = entry.getKey();
                            List<String> moviesIds = entry.getValue();

                            MoviesList movieList = new MoviesList(key, moviesIds);
                            movieLists.add(movieList);
                        }
                        // Clear the DAO and insert the new list
                        dao.clear();
                        dao.insertList(movieLists);

                        movieDao.clear();

                        fetchMovieDetails(movieLists);
                    }).start();
                }

                @Override
                public void onFailure(String errorMessage) {
                    System.err.println("Error fetching moviesLists " + ": " + errorMessage);
                }
            });
        }

        /**
         * Fetches movie details for each `MoviesList` item asynchronously.
         */
        public void fetchMovieDetails(@NonNull List<MoviesList> data) {
            Set<String> uniqueMovieIds = new HashSet<>();

            for (MoviesList item : data) {
                uniqueMovieIds.addAll(item.getMovieIds());
            }

            // Fetch movie details efficiently
            Map<String, Movie> movieMap = fromIdsToMovies(new ArrayList<>(uniqueMovieIds));

            // Assign fetched movies to each MoviesList
            for (MoviesList item : data) {
                List<Movie> filteredMovies = item.getMovieIds().stream()
                        .map(movieMap::get) // Get movie from map
                        .filter(Objects::nonNull) // Avoid nulls
                        .collect(Collectors.toList());
                if (filteredMovies.isEmpty()) {
                    data.remove(item);
                } else {
                    item.setMovieList(filteredMovies);
                }
            }
            postValue(data);
        }

        public Map<String, Movie> fromIdsToMovies(List<String> movieIds) {
            if (movieIds.isEmpty()) return Collections.emptyMap();

            ConcurrentHashMap<String, Movie> movieMap = new ConcurrentHashMap<>();
            CountDownLatch latch = new CountDownLatch(movieIds.size());

            for (String movieId : movieIds) {
                Movie cachedMovie = movieDao.getMovieById(movieId);
                if (cachedMovie != null) {
                    movieMap.put(movieId, cachedMovie);
                    latch.countDown();
                } else {
                    moviesApi.getMovieById(movieId, new MovieCallback() {
                        @Override
                        public void onSuccess(Movie movie) {
                            movieMap.put(movieId, movie);
                            executorService.execute(() -> movieDao.insertMovie(movie));
                            latch.countDown();
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            System.err.println("Error fetching movie: " + errorMessage);
                            latch.countDown();
                        }
                    });
                }
            }

            try {
                boolean completed = latch.await(5, TimeUnit.SECONDS); // Prevent infinite wait
                if (!completed) {
                    System.err.println("Timeout waiting for movie fetch.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            return movieMap;
        }
    }

    public LiveData<List<MoviesList>> getAll() {
        return moviesListData;
    }

    public void reload() {
        moviesListData.fetchMoviesFromApi();
    }
}
