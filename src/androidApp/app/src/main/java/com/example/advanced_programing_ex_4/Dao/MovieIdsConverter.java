package com.example.advanced_programing_ex_4.Dao;

import androidx.room.TypeConverter;

import com.example.advanced_programing_ex_4.api.MoviesListsApi;
import com.example.advanced_programing_ex_4.entities.Movie;
import com.example.advanced_programing_ex_4.api.MoviesApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieIdsConverter {
    @TypeConverter
    public static List<String> fromMovieList(List<Movie> movieList) {
        if (movieList == null || movieList.isEmpty()) return null;

        List<String> movieIds = new ArrayList<>();
        for (Movie movie : movieList) {
            movieIds.add(movie.getMovieId());  // Extracting the movieId from each Movie object
        }
        return movieIds;
    }

    @TypeConverter
    public static List<Movie> fromMovieIds(List<String> movieIds) {
        if (movieIds == null || movieIds.isEmpty()) return new ArrayList<>();

        List<Movie> movies = new ArrayList<>();
        MoviesApi moviesApi = new MoviesApi();

        // Ideally, this should be done asynchronously, so here we're just adding logic as a placeholder
        for (String movieId : movieIds) {
            // You will need an asynchronous call to fetch each movie from the API
            Movie movie = moviesApi.getMovieById(movieId);
            if (movie != null) {
                movies.add(movie);
            }
        }
        return movies;
    }
}
