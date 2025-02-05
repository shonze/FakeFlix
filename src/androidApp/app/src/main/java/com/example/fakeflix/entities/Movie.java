package com.example.fakeflix.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "movies")
public class Movie implements Serializable {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    @SerializedName("_id")
    private String movieId;
    private String title;
    private List<String> categories;
    private String description;
    private String length;
    private String thumbnail;
    private String thumbnailName;

    private String video;

    private String videoName;

    public Movie(@NonNull String movieId, String title, List<String> categories, String description, String length, String thumbnail, String thumbnailName, String video, String videoName) {
        this.movieId = movieId;
        this.title = title;
        this.categories = categories;
        this.description = description;
        this.length = length;
        this.thumbnail = thumbnail;
        this.thumbnailName = thumbnailName;
        this.video = video;
        this.videoName = videoName;
    }

    @NonNull
    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(@NonNull String movieId) {
        this.movieId = movieId;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnailName() {
        return thumbnailName;
    }

    public void setThumbnailName(String thumbnailName) {
        this.thumbnailName = thumbnailName;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }
}

//
//@Entity(tableName = "movies")
//public class MovieEntity {
//    @PrimaryKey(autoGenerate = true)
//    public int id;
//
//    @SerializedName("_id")
//    public String movieId;
//
//
//    public void setThumbnailName(String thumbnailName) {
//        this.thumbnailName = thumbnailName;
//    }
//
//    public String thumbnailName;
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getVideoName() {
//        return videoName;
//    }
//
//    public void setVideoName(String videoName) {
//        this.videoName = videoName;
//    }
//
//    public String videoName;
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String title;
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String description;
//
//    public int getLength() {
//        return length;
//    }
//
//    public void setLength(int length) {
//        this.length = length;
//    }
//
//    public int length;
//
//    public String getThumbnailName() {
//        return thumbnail;
//    }
//
//    public void setThumbnail(String thumbnail) {
//        this.thumbnail = thumbnail;
//    }
//
//    public String getId() {
//        return movieId;
//    }
//
//    public String thumbnail;
//
//    public String getVideoUrl() {
//        return video;
//    }
//
//    public void setVideoUrl(String videoUrl) {
//        this.video = videoUrl;
//    }
//
//    public String video;
//
//    public List<String> getCategories() {
//        return categories;
//    }
//
//    public void setCategories(List<String> categories) {
//        this.categories = categories;
//    }
//
//    public List<String> categories;
//
//}