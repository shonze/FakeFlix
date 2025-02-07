package com.example.advanced_programing_ex_4.Repositories;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.advanced_programing_ex_4.Dao.AppDB;
import com.example.advanced_programing_ex_4.Dao.CategoriesDao;
import com.example.advanced_programing_ex_4.Dao.MovieDao;
import com.example.advanced_programing_ex_4.api.CategoriesApi;
import com.example.advanced_programing_ex_4.api.MovieCallback;
import com.example.advanced_programing_ex_4.api.MoviesApi;
import com.example.advanced_programing_ex_4.entities.Category;
import com.example.advanced_programing_ex_4.entities.Movie;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class CategoriesRepository {
    private final CategoriesDao dao;
    private final MovieDao movieDao;
    private final CategoriesData categoriesData;
    private final CategoriesApi categoriesApi;
    private final MoviesApi moviesApi;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public CategoriesRepository(Context context) {
        AppDB db = AppDB.getInstance(context);
        this.dao = db.categoriesDao();
        this.movieDao = db.movieDao();
        this.categoriesData = new CategoriesData();
        this.categoriesApi = new CategoriesApi(dao, categoriesData);
        this.moviesApi = new MoviesApi();
    }

    public class CategoriesData extends MutableLiveData<List<Category>> {
        public CategoriesData() {
            super();
            setValue(new LinkedList<>()); // Ensuring an empty list to prevent null issues
        }

        @Override
        protected void onActive() {
            super.onActive();
            executorService.execute(() -> {
                List<Category> data = dao.get();
                if (data == null || data.isEmpty()) {
                    fetchMoviesFromApi();
                } else {
                    fetchMovieDetails(data);
                }
            });
        }

        /**
         * Fetches movies from the API when no data is available in the database.
         */
        private void fetchMoviesFromApi() {
            categoriesApi.get();
        }

        /**
         * Fetches movie details for each `Category` item asynchronously.
         */
        public void fetchMovieDetails(@NonNull List<Category> data) {
            if (data == null || data.isEmpty()) {
                postValue(Collections.emptyList());
                return;
            }

            Set<String> uniqueMovieIds = new HashSet<>();
            for (Category item : data) {
                if (item != null && item.getMoviesIds() != null) {
                    uniqueMovieIds.addAll(item.getMoviesIds());
                }
            }

            // Fetch movie details efficiently
            Map<String, Movie> movieMap = fromIdsToMovies(new ArrayList<>(uniqueMovieIds));

            List<Category> filteredCategories = new ArrayList<>();
            for (Category item : data) {
                if (item == null || item.getMoviesIds() == null) continue;

                List<Movie> filteredMovies = item.getMoviesIds().stream()
                        .map(movieMap::get) // Get movie from map
                        .filter(Objects::nonNull) // Avoid nulls
                        .collect(Collectors.toList());

                if (!filteredMovies.isEmpty()) {
                    item.setMovies(filteredMovies);
                    filteredCategories.add(item);
                }
            }

            postValue(filteredCategories);
        }

        /**
         * Converts a list of movie IDs to a map of movie details.
         */
        public Map<String, Movie> fromIdsToMovies(List<String> movieIds) {
            if (movieIds == null || movieIds.isEmpty()) return Collections.emptyMap();

            ConcurrentHashMap<String, Movie> movieMap = new ConcurrentHashMap<>();
            CountDownLatch latch = new CountDownLatch(movieIds.size());

            for (String movieId : movieIds) {
                if (movieId == null) {
                    latch.countDown();
                    continue;
                }

                Movie cachedMovie = movieDao.getMovieById(movieId);

                if (cachedMovie != null) {
                    movieMap.put(movieId, cachedMovie);
                    latch.countDown();
                } else {
                    moviesApi.getMovieById(movieId, new MovieCallback() {
                        @Override
                        public void onSuccess(Movie movie) {
                            if (movie != null) {
                                movieMap.put(movieId, movie);
                                executorService.execute(() -> movieDao.insertMovie(movie));
                            }
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

    public LiveData<List<Category>> getAll() {
        return categoriesData;
    }
}
