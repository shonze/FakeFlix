package com.example.fakeflix;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.advanced_programing_ex_4.MainActivity;
import com.example.fakeflix.databinding.ActivityStartingBinding;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartingActivity extends AppCompatActivity {
    private UserDB db;
    private UserDetails userDetails;
    UserDetailsDao userDetailsDao;
    private ActivityStartingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user should be remembered
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String rememberMe = sharedPreferences.getString("rememberMe", "false");
        String jwtToken = sharedPreferences.getString("jwtToken", null);

        if ("true".equals(rememberMe) && jwtToken != null) {
            bringData(jwtToken);
            // Redirect to homepage if needed
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish(); // Prevent returning to LoginActivity
        }

        setContentView(R.layout.activity_starting);

        binding = ActivityStartingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.getStartedButton.setOnClickListener(view -> {
            // Navigate to RegisterActivity
            Intent intent = new Intent(StartingActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        binding.signInButton.setOnClickListener(view -> {
            // Navigate to LoginActivity
            Intent intent = new Intent(StartingActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }


    private void bringData(String token) {

        ApiService apiService = RetrofitClient.getApiService();
        String fixed = "Bearer " + token;
        Call<ResponseBody> call = apiService.getUserDetails(fixed);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String photoUrl,fullName;
                    try {
                        // You may need to parse the response if it's in JSON format
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        photoUrl = jsonResponse.getString("userPicture");
                        fullName = jsonResponse.getString("userFullName");

                        db = Room.databaseBuilder(getApplicationContext(),
                                        UserDB.class, "UserDB")
                                .allowMainThreadQueries().build();
                        userDetailsDao = db.userDetailsDao();
                        handleSave(fullName, photoUrl);

                    } catch (Exception e) {
                        Log.e("Upload", "Error parsing response: " + e.getMessage());
                    }

                } else {
                    Toast.makeText(StartingActivity.this, "Bring data failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(StartingActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleSave(String fullName, String photoUrl) {
        // Retrieve the existing user (assuming only one user is stored)
        userDetails = userDetailsDao.index();

        if (userDetails == null) {
            // Insert a new user if none exists
            userDetails = new UserDetails(fullName,photoUrl);
            userDetailsDao.insert(userDetails);
        } else {
            // Update the existing user instead of adding a new one
            userDetails.setUserFullName(fullName);
            userDetails.setUserPhotoUrl(photoUrl);
            userDetailsDao.update(userDetails);
        }
        finish();
    }
}