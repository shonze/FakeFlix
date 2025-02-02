package com.example.advanced_programing_ex_4.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
@Entity
public class MoviesList {
    @PrimaryKey(autoGenerate = true)
    private String moviesTitle;
    private List<Movie> moviesList;

    public MoviesList(String moviesTitle, List<Movie> moviesList) {
        this.moviesTitle = moviesTitle;
        this.moviesList = moviesList;
    }
    public List<Movie> getMoviesList() {
        return moviesList;
    }

    public void setMoviesList(List<Movie> moviesList) {
        this.moviesList = moviesList;
    }

    public String getMoviesTitle() {
        return moviesTitle;
    }

    public void setCategoryName(String moviesTitle) {
        this.moviesTitle = moviesTitle;
    }
}
