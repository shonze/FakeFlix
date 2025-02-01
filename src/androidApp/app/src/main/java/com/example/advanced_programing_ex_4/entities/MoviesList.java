package com.example.advanced_programing_ex_4.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
@Entity
public class MoviesList {
    @PrimaryKey(autoGenerate = true)
    private String categoryName;
    private List<Movie> moviesList;

    public MoviesList(String categoryName, List<Movie> moviesList) {
        this.categoryName = categoryName;
        this.moviesList = moviesList;
    }
    public List<Movie> getMoviesList() {
        return moviesList;
    }

    public void setMoviesList(List<Movie> moviesList) {
        this.moviesList = moviesList;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
