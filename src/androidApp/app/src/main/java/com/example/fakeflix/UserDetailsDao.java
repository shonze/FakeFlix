package com.example.fakeflix;


import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomDatabase;
import androidx.room.Update;

import java.util.List;


@Dao
public interface UserDetailsDao {
    @Query("SELECT * FROM userDetails")
    UserDetails index();
    @Query("SELECT * FROM userDetails WHERE id = :id")
    UserDetails get(int id);

    @Insert
    void insert(UserDetails... userDetailss);

    @Update
    void update(UserDetails... userDetailss);

    @Delete
    void delete(UserDetails... userDetailss);
}