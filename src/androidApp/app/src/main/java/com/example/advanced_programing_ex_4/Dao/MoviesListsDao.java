package com.example.advanced_programing_ex_4.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.advanced_programing_ex_4.entities.MoviesList;

import java.util.List;

@Dao
public interface MoviesListsDao {
    @Query("SELECT * FROM MoviesList")
    List<MoviesList> index();

    @Query("SELECT * FROM MoviesList WHERE id = :id")
    MoviesList get(int id);

    @Insert
    void insert(MoviesList... moviesLists);

    @Update
    void update(MoviesList... moviesLists);

    @Delete
    void delete(MoviesList... moviesLists);
}
