package com.example.advanced_programing_ex_4.ResnponseEntities;

import com.example.advanced_programing_ex_4.entities.MoviesList;

import java.util.List;
import java.util.Map;

public class MovieListResponse {
    private Map<String, List<String>> moviesLists;

    public MovieListResponse(Map<String, List<String>> moviesLists) {
        this.moviesLists = moviesLists;
    }

    public Map<String, List<String>> getMoviesLists() {
        return moviesLists;
    }

    public void setMoviesLists(Map<String, List<String>> moviesLists) {
        this.moviesLists = moviesLists;
    }
}
