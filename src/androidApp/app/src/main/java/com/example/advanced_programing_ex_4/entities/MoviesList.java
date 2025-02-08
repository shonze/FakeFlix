package com.example.advanced_programing_ex_4.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.advanced_programing_ex_4.Dao.MovieIdsConverter;

import java.util.ArrayList;
import java.util.List;

@Entity
@TypeConverters(MovieIdsConverter.class)
public class MoviesList {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String moviesTitle;

    private List<String> movieIds;
    @Ignore
    private List<Movie> movieList;

    public List<Movie> getMovieList() {
        return movieList;
    }

    public List<String> getMovieIds() {
        return movieIds;
    }

    public void setMovieIds(List<String> movieIds) {
        this.movieIds = movieIds;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public void setMoviesTitle(@NonNull String moviesTitle) {
        this.moviesTitle = moviesTitle;
    }

    // Constructor with movie IDs
    public MoviesList(@NonNull String moviesTitle, List<String> movieIds) {
        this.moviesTitle = moviesTitle;
        this.movieIds = movieIds != null ? movieIds : new ArrayList<>();  // Ensuring non-null list
        this.movieList = new ArrayList<>();  // Initialize empty list for Movie objects
    }

    @NonNull
    public String getMoviesTitle() {
        return moviesTitle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
