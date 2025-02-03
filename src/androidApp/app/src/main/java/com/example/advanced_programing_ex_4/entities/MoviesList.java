package com.example.advanced_programing_ex_4.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.advanced_programing_ex_4.Dao.MovieIdsConverter;

import java.util.ArrayList;
import java.util.List;

@Entity
@TypeConverters(MovieIdsConverter.class)
public class MoviesList {
    @PrimaryKey
    @NonNull
    private String moviesTitle;
    private List<String> movieIds;

    public void setMoviesTitle(@NonNull String moviesTitle) {
        this.moviesTitle = moviesTitle;
    }

    public List<String> getMovieIds() {
        return movieIds;
    }

    public void setMovieIds(List<String> movieIds) {
        this.movieIds = movieIds;
    }

    public MoviesList(@NonNull String moviesTitle, List<String> movieIds) {
        this.moviesTitle = moviesTitle;
        this.movieIds = movieIds;
    }

    @NonNull
    public String getMoviesTitle() {
        return moviesTitle;
    }
}
