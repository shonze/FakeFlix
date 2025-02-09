package com.example.fakeflix;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.room.Room;

import com.example.fakeflix.R;
import com.example.advanced_programing_ex_4.MainActivity;
import com.example.fakeflix.databinding.ActivityLoginBinding;
import org.json.JSONObject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private UserDB db;
    private UserDetails userDetails;
    UserDetailsDao userDetailsDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.etUsername.setTextCursorDrawable(R.drawable.cursor_white);
            binding.etPassword.setTextCursorDrawable(R.drawable.cursor_white);
        }

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String username = binding.etUsername.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        boolean rememberMe = binding.checkboxRememberMe.isChecked();
        if(!validateFields(username,password)) {
            return;
        }

        Login login = new Login(username, password);

        ApiService apiService = RetrofitClient.getApiService();
        Call<AuthResponse> call = apiService.loginUser(login);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    if (token != null) {
                        saveToken(token);
                        if (rememberMe) {
                            rememberMe();
                        }
                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        bringData(token);
                        checkValidation(token);
                        // Navigate to HomeActivity if needed
//                         startActivity(new Intent(LoginActivity.this, SearchActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "Error: " + response.body().getErrors(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkValidation(String jwtToken) {
        if (jwtToken == null) {
            Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();
        Call<ResponseBody> call = apiService.validateToken("Bearer " + jwtToken);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Convert response body to JSON and extract isAdmin
                        String jsonString = response.body().string();
                        JSONObject jsonObject = new JSONObject(jsonString);
                        String isAdmin = jsonObject.getString("isAdmin");

                        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("isAdmin", isAdmin);
                        editor.apply();

                        // Pass isAdmin to the next activity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("isAdmin", isAdmin);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void saveToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("jwtToken", token);
        editor.apply();
    }

    private void rememberMe() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("rememberMe", "true");
        editor.apply();
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

                        photoUrl= jsonResponse.getString("userPicture");
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
                    Toast.makeText(LoginActivity.this, "Bring data failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
    private boolean validateFields(String username, String password ) {
        boolean isValid = true;

//        String username = binding.etUsername.getText().toString().trim();
//        String password = binding.etPassword.getText().toString().trim();

        if (username.isEmpty()) {
            binding.etUsername.setError("Username is required");
            isValid = false;
        }
        // Validate Password
        if (password.isEmpty()) {
            binding.etPassword.setError("Password is required");
            isValid = false;
        }
        return isValid;
    }
}