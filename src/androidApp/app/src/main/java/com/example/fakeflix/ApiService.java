package com.example.fakeflix;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("users")  // This appends "/api/users" to the base URL
    Call<AuthResponse> registerUser(@Body User user);
}
