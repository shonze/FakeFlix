package com.example.advanced_programing_ex_4.api;

import com.example.advanced_programing_ex_4.entities.Movie;

import java.util.List;
import java.util.Map;

public interface MovieListCallback {
    void onSuccess(Map<String, List<String>> moviesLists);

    void onFailure(String errorMessage);
}
