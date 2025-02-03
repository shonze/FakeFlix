package com.example.fakeflix;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserDetails {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String userPhotoUrl;
    private String userFullName;

    public UserDetails(String userFullName, String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
        this.userFullName = userFullName;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
