package com.example.advanced_programing_ex_4.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.advanced_programing_ex_4.Repositories.CategoriesRepository;
import com.example.advanced_programing_ex_4.entities.Category;
import com.example.advanced_programing_ex_4.entities.MoviesList;

import java.util.List;
@Dao
public interface CategoriesDao {
    @Query("SELECT * FROM Category")
    List<Category> get();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Category... category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<Category> moviesLists);

    @Query("DELETE FROM Category")
    void clear();  // Clears all records from the table

    @Update
    void update(MoviesList... moviesLists);
}
