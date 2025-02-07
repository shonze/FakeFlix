package com.example.advanced_programing_ex_4.Dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.advanced_programing_ex_4.entities.Category;
import com.example.advanced_programing_ex_4.entities.Movie;
import com.example.advanced_programing_ex_4.entities.MoviesList;

@Database(entities = {MoviesList.class, Movie.class, Category.class}, version = 15)
@TypeConverters(MovieIdsConverter.class) // Register the TypeConverter
public abstract class AppDB extends RoomDatabase {
    private static volatile AppDB instance;

    public static AppDB getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDB.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDB.class, "movie_database")
                            .fallbackToDestructiveMigration()  // Allows destructive migration (erases data)
                            .build();
                }
            }
        }
        return instance;
    }

    public abstract MoviesListsDao moviesListsDao();

    public abstract MovieDao movieDao();

    public abstract CategoriesDao categoriesDao();
}
