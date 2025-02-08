package com.example.advanced_programing_ex_4.Dao;

import androidx.room.TypeConverter;

import com.example.advanced_programing_ex_4.api.MoviesListsApi;
import com.example.advanced_programing_ex_4.entities.Movie;
import com.example.advanced_programing_ex_4.api.MoviesApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//public class MovieIdsConverter {
//    @TypeConverter
//    public static String fromMovieList(List<Movie> movieList) {
//        if (movieList == null || movieList.isEmpty()) return "";
//
//        List<String> movieIds = new ArrayList<>();
//        for (Movie movie : movieList) {
//            movieIds.add(movie.getMovieId());  // Extracting the movieId from each Movie object
//        }
//        return String.join(",", movieIds); // Convert list to a single string
//    }
//
//    @TypeConverter
//    public static List<Movie> fromMovieIds(String movieIdsString) {
//        if (movieIdsString == null || movieIdsString.isEmpty()) return new ArrayList<>();
//
//        List<String> movieIds = Arrays.asList(movieIdsString.split(",")); // Convert string back to List<String>
//
//        List<Movie> movies = new ArrayList<>();
//        MoviesApi moviesApi = new MoviesApi();
//
//        // Fetch each movie from API
//        for (String movieId : movieIds) {
//            Movie movie = moviesApi.getMovieById(movieId);
//            if (movie != null) {
//                movies.add(movie);
//            }
//        }
//        return movies;
//    }
//}
public class MovieIdsConverter {
    @TypeConverter
    public static String fromMovieIdList(List<String> movieIds) {
        return (movieIds == null || movieIds.isEmpty()) ? "" : String.join(",", movieIds);
    }

    @TypeConverter
    public static List<String> toMovieIdList(String movieIdsString) {
        return (movieIdsString == null || movieIdsString.isEmpty()) ? new ArrayList<>()
                : Arrays.asList(movieIdsString.split(","));
    }
}