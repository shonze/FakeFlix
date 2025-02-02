package com.example.advanced_programing_ex_4.Dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.advanced_programing_ex_4.entities.TopMovie;

import java.util.List;

@Dao
public interface TopMovieDao {
    @Query("SELECT * FROM TopMovie")
    List<TopMovie> get();
}
