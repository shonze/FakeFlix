package com.example.fakeflix;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {UserDetails.class}, version = 1)
public abstract class UserDB extends RoomDatabase {
    public abstract UserDetailsDao userDetailsDao();
}