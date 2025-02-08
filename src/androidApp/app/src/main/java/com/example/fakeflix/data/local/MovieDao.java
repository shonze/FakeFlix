package com.example.fakeflix.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert
    void insertMovie(MovieEntity movie);

    @Query("SELECT * FROM movies")
    List<MovieEntity> getAllMovies();

    @Query("SELECT * FROM movies WHERE id = :id")
    MovieEntity getMovieById(String id);

    @Update
    void updateMovie(MovieEntity movie);

    @Query("DELETE FROM movies WHERE id= :id")
    void deleteMovie(String id);

    // Add this method to delete all movies
    @Query("DELETE FROM movies")
    void deleteAllMovies();
}