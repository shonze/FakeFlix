package com.example.fakeflix.data.local;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import android.content.Context;

import com.example.fakeflix.utils.Converters;

@Database(entities = {CategoryEntity.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class CategoryDatabase extends RoomDatabase {
    private static volatile CategoryDatabase instance;

    public abstract CategoryDao categoryDao();

    public static CategoryDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (CategoryDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    CategoryDatabase.class, "category_database")
                            .build();
                }
            }
        }
        return instance;
    }
}