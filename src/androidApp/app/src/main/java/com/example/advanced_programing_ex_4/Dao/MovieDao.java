package com.example.advanced_programing_ex_4.Dao;

import androidx.room.Dao;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.advanced_programing_ex_4.entities.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT COUNT(*) FROM Movie")
    int getMoviesCount();

    @Query("SELECT * FROM Movie WHERE movieId = :id")
    Movie getMovieById(String id);

    @Query("SELECT movieId FROM Movie")
    List<String> getAllMovieIds();

    @Query("SELECT * FROM Movie")
    List<Movie> getAllMovies();

    @Insert
    void insertMovie(Movie... movie);
}
