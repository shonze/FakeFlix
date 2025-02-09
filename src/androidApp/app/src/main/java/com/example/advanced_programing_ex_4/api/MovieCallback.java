package com.example.advanced_programing_ex_4.api;

import com.example.advanced_programing_ex_4.entities.Movie;

public interface MovieCallback {
    void onSuccess(Movie movie);
    void onFailure(String errorMessage);
}
