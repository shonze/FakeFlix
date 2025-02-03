//package com.example.advanced_programing_ex_4.Repositories;
//
//import android.content.Context;
//import android.util.Log;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.Observer;
//
//import com.example.advanced_programing_ex_4.Dao.AppDB;
//import com.example.advanced_programing_ex_4.Dao.MovieDao;
//import com.example.advanced_programing_ex_4.api.MoviesApi;
//import com.example.advanced_programing_ex_4.entities.Movie;
//import com.example.advanced_programing_ex_4.entities.MoviesList;
//
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Random;
//
//public class MovieRepository {
//    private final MovieDao dao;
//    private final MoviesData allMoviesLiveData;
//    private final MoviesApi moviesApi;
//
//    public MovieRepository(Context context) {
//        AppDB db = AppDB.getInstance(context);
//        dao = db.movieDao();
//        allMoviesLiveData = new MoviesData();
//        moviesApi = new MoviesApi(allMoviesLiveData, dao);
//    }
//
//    // Fetches a random movie when needed
//    public Movie getRandomMovie() {
//        int count = dao.getMoviesCount();
//        if (count == 0) return null;
//
//        int randomIndex = new Random().nextInt(count);
//        List<String> allMovieIds = dao.getAllMovieIds();
//        String randomId = allMovieIds.get(randomIndex);
//        return dao.getMovieById(randomId);
//    }
//
//    // Fetch all movies and post them to LiveData
//    class MoviesData extends MutableLiveData<List<Movie>> {
//        public MoviesData() {
//            super();
//        }
//
//        @Override
//        protected void onActive() {
//            super.onActive();
//            new Thread(() -> {
//                List<Movie> movies = dao.getAllMovies(); // Get all movies from the database
//                postValue(movies); // Post the list to LiveData
//            }).start();
//        }
//
//        // Method to add or update a single movie in the list
//        public void addOrUpdateMovie(Movie movie) {
//            List<Movie> currentMovies = getValue();
//            if (currentMovies == null) {
//                currentMovies = new ArrayList<>();
//            }
//            // Check if the movie exists and update, otherwise add the new movie
//            boolean updated = false;
//            for (int i = 0; i < currentMovies.size(); i++) {
//                if (currentMovies.get(i) != null) {
//                    if (currentMovies.get(i).getMovieId().equals(movie.getMovieId())) {
//                        currentMovies.set(i, movie);
//                        updated = true;
//                        break;
//                    }
//                }
//            }
//            if (!updated) {
//                currentMovies.add(movie);
//            }
//            postValue(currentMovies); // Update the LiveData with the modified list
//        }
//    }
//
//    // Get LiveData for all movies
//    public LiveData<List<Movie>> getAllMovies() {
//        return allMoviesLiveData;
//    }
//
//    // Get a movie by ID (and update the list if necessary)
//    public LiveData<List<Movie>> getMoviesByIds(List<String> movieIds) {
//        MutableLiveData<List<Movie>> filteredMoviesLiveData = new MutableLiveData<>();
//
//        // Observe the existing LiveData to get the full list of movies
//        allMoviesLiveData.observeForever(movies -> {
//            List<String> unseenIds = movieIds;
//            List<Movie> filteredMovies = new ArrayList<>();
//            while (!unseenIds.isEmpty()) {
//                if (movies != null && !movies.isEmpty()) {
//                    // Loop through movie IDs and filter the corresponding movies from the full list
//                    for (String movieId : movieIds) {
//                        for (Movie movie : movies) {
//                            if (movie != null) {
//                                if (movie.getMovieId().equals(movieId)) {
//                                    filteredMovies.add(movie);
//                                    unseenIds.remove(movieId);
//                                    break; // Once we find the movie, stop looking for it
//                                }
//                            }
//                        }
//                    }
//                    // Post the filtered list of movies to the new LiveData
//                    filteredMoviesLiveData.postValue(filteredMovies);
//                } else {
//                    // If no movies in the list, post an empty list or handle error
//                    filteredMoviesLiveData.postValue(new ArrayList<>());
//                }
//                for (String missingId : unseenIds) {
//                    moviesApi.getMovieById(missingId);
//                }
//            }
//        });
//
//        return filteredMoviesLiveData;
//    }
//
//}
