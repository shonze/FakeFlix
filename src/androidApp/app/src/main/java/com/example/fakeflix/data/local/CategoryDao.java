package com.example.fakeflix.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insertCategory(CategoryEntity category);

    @Query("SELECT * FROM categories")
    List<CategoryEntity> getAllCategories();

    @Query("SELECT * FROM categories WHERE id = :id")
    CategoryEntity getCategoryById(String id);

    @Query("SELECT categoryId FROM categories WHERE name = :name")
    String getCategoryIdByName(String name);

    @Update
    void updateCategory(CategoryEntity category);

    @Delete
    void deleteCategory(CategoryEntity category);

    // Add this method to delete all categories
    @Query("DELETE FROM categories")
    void deleteAllCategories();
}