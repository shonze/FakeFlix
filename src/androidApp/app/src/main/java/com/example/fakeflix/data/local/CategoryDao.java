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
    void insertCategory(com.example.fakeflix.data.local.CategoryEntity category);

    @Query("SELECT * FROM categories")
    List<com.example.fakeflix.data.local.CategoryEntity> getAllCategories();

    @Query("SELECT * FROM categories WHERE id = :id")
    com.example.fakeflix.data.local.CategoryEntity getCategoryById(String id);

    @Query("SELECT categoryId FROM categories WHERE name = :name")
    String getCategoryIdByName(String name);

    @Update
    void updateCategory(com.example.fakeflix.data.local.CategoryEntity category);

    @Delete
    void deleteCategory(com.example.fakeflix.data.local.CategoryEntity category);

    // Add this method to delete all categories
    @Query("DELETE FROM categories")
    void deleteAllCategories();
}