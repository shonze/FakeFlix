package com.example.advanced_programing_ex_4.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Entity
public class Category implements Serializable {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    @SerializedName("_id")
    private String categoryId;
    private String name;
    private String description;
    @SerializedName("movies")
    private List<String> moviesIds;
    @Ignore
    private transient List<Movie> movies;

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    @NonNull
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(@NonNull String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getMoviesIds() {
        return moviesIds;
    }

    public void setMoviesIds(List<String> moviesIds) {
        this.moviesIds = moviesIds;
    }

    public Category(@NonNull String categoryId, String name, String description, List<String> moviesIds) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.moviesIds = moviesIds;
    }
}
