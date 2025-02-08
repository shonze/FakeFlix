package com.example.fakeflix.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.fakeflix.entities.Movie;
import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%'")
    LiveData<List<Movie>> searchMovies(String query);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovies(List<Movie> movies);
}

