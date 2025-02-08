package com.example.netflixadmin.data.local;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import android.database.Cursor;

import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.netflixadmin.utils.Converters;

@Database(entities = {MovieEntity.class}, version = 3) // Current version is 3
@TypeConverters({Converters.class})
public abstract class MovieDatabase extends RoomDatabase {
    private static volatile MovieDatabase instance;

    public abstract MovieDao movieDao();

    public static MovieDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (MovieDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), MovieDatabase.class, "movies_database")
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3) // Add both migrations here
                            .build();
                }
            }
        }
        return instance;
    }

    // Migration from version 1 to 2
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Check if the 'video' column already exists in the movies table
            Cursor cursor = database.query("PRAGMA table_info(movies)");
            boolean videoExists = false;

            // Iterate through the columns to check for 'video'
            while (cursor.moveToNext()) {
                String columnName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                if ("video".equals(columnName)) {
                    videoExists = true;
                    break;
                }
            }
            cursor.close();

            // Only add the video column if it doesn't already exist
            if (!videoExists) {
                database.execSQL("ALTER TABLE movies ADD COLUMN video TEXT");
            }
        }
    };

    // Migration from version 2 to 3 remains unchanged
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        public void migrate(SupportSQLiteDatabase database) {
            // Rename table if necessary
            // Add new columns without altering existing data
            database.execSQL("ALTER TABLE movies ADD COLUMN videoName TEXT DEFAULT NULL;");
            database.execSQL("ALTER TABLE movies ADD COLUMN thumbnailName TEXT DEFAULT NULL;");
        }
    };


}
