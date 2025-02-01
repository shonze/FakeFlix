package com.example.fakeflix;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("token")
    private String token;

    @SerializedName("errors")
    private String errors;

    public String getToken() {
        return token;
    }

    public String getErrors() {
        return errors;
    }
}
