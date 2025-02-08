package com.example.advanced_programing_ex_4.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.advanced_programing_ex_4.entities.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT COUNT(*) FROM Movie")
    int getMoviesCount();

    @Query("SELECT * FROM Movie WHERE movieId = :id")
    Movie getMovieById(String id);

    @Query("SELECT * FROM Movie WHERE movieId IN (:movieIds)")
    LiveData<List<Movie>> getMoviesByIds(List<String> movieIds);

    @Query("SELECT * FROM Movie WHERE movieId IN (:movieIds)")
    List<Movie> getMoviesByIdsSync(List<String> movieIds);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovies(List<Movie> movies);

    @Query("SELECT movieId FROM Movie")
    List<String> getAllMovieIds();

    @Query("SELECT * FROM Movie")
    List<Movie> getAllMovies();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie... movie);

    @Query("DELETE FROM Movie")
    void clear();
}
