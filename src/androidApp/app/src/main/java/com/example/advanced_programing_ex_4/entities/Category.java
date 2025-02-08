package com.example.advanced_programing_ex_4.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public class Category implements Parcelable {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    @SerializedName("_id")
    private String categoryId;
    private String name;
    private String description;
    @SerializedName("movies")
    private List<String> moviesIds;
    @Ignore
    private List<Movie> movies;

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

    // Parcelable Implementation
    protected Category(Parcel in) {
        categoryId = in.readString();
        name = in.readString();
        description = in.readString();
        moviesIds = in.createStringArrayList();
        // movies field remains transient, so it is not serialized.
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(categoryId);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeStringList(moviesIds);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
