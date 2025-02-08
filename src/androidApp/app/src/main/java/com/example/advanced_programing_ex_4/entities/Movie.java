package com.example.advanced_programing_ex_4.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Movie implements Serializable {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    @SerializedName("_id")
    private String movieId;

    @NonNull
    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(@NonNull String movieId) {
        this.movieId = movieId;
    }

    private String title;

    private List<String> categories;

    private String description;

    private String length;
    private String thumbnailName;
    private String videoName;

    public String getThumbnailName() {
        return thumbnailName;
    }

    public void setThumbnailName(String thumbnailName) {
        this.thumbnailName = thumbnailName;

    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public Movie(@NonNull String movieId, String title, List<String> categories, String description,
                 String length, String thumbnailName, String videoName) {
        this.movieId = movieId;
        this.title = title;
        this.categories = categories;
        this.description = description;
        this.length = length;
        this.thumbnailName = thumbnailName;
        this.videoName = videoName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}
