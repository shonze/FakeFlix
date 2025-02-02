package com.example.advanced_programing_ex_4.Dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.advanced_programing_ex_4.entities.Movie;

@Dao
public interface MovieDao {
    @Query("SELECT COUNT(*) FROM Movie")
    int getMoviesCount();

    @Query("SELECT * FROM Movie WHERE movieId = :randomId")
    Movie getMovieById(String randomId);
}
