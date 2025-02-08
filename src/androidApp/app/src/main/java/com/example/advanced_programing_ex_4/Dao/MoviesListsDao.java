package com.example.advanced_programing_ex_4.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.advanced_programing_ex_4.entities.MoviesList;

import java.util.List;

@Dao
public interface MoviesListsDao {
    @Query("SELECT * FROM MoviesList")
    List<MoviesList> get();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMoviesList(MoviesList moviesList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<MoviesList> moviesLists);  // Inserts a list of MoviesList

    @Query("DELETE FROM MoviesList")
    void clear();  // Clears all records from the table

    @Update
    void update(MoviesList... moviesLists);

    @Delete
    void delete(MoviesList... moviesLists);
}