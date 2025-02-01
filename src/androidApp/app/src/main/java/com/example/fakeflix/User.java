package com.example.fakeflix;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("fullName")
    private String fullName;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("birthdate")
    private String birthdate;

    @SerializedName("password")
    private String password;

    @SerializedName("photoName")
    private String photoName;

    @SerializedName("photoUrl")
    private String photoUrl;

    public User(String fullName, String username, String email, String birthdate,
                String password, String photoName, String photoUrl) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.birthdate = birthdate;
        this.password = password;
        this.photoName = photoName;
        this.photoUrl = photoUrl;
    }
}

