package com.example.netflixadmin.data.local;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import androidx.room.TypeConverters;

import com.example.netflixadmin.utils.Converters;

@Database(entities = {MovieEntity.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class MovieDatabase extends RoomDatabase {
    private static volatile MovieDatabase instance;

    public abstract MovieDao movieDao();

    public static MovieDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (MovieDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    MovieDatabase.class, "movie_database")
                            .build();
                }
            }
        }
        return instance;
    }
}
