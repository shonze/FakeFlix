package com.example.advanced_programing_ex_4.entities;

import java.util.ArrayList;

public class Movie {
    private String title;

    private ArrayList<String> categories;

    private String description;

    private String length;

    private String thumbnail;

    private String video;

//    public Movie(String title, ArrayList<String> categories, String description,
//                 String length, String thumbnail, String video) {
public Movie(String title){
        this.title = title;
//        this.categories = categories;
//        this.description = description;
//        this.length = length;
//        this.thumbnail = thumbnail;
//        this.video = video;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
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

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
