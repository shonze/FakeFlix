package com.example.advanced_programing_ex_4.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TopMovie {
    @PrimaryKey(autoGenerate = true)

    Movie movie;

    public TopMovie(Movie movie) {
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
