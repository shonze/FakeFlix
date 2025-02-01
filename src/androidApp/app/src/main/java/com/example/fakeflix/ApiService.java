package com.example.fakeflix;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @POST("users")  // This appends "/api/users" to the base URL
    Call<AuthResponse> registerUser(@Body User user);

    @POST("users/check")
    Call<AuthResponse> checkUser(@Body User user);

    @Multipart
    @POST("file")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part image);
}
