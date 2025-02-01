package com.example.fakeflix;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;

import com.example.fakeflix.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.fakeflix.databinding.ActivityRegisterBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;

    private static final int PICK_IMAGE = 100;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.etFullName.setTextCursorDrawable(R.drawable.cursor_white);
            binding.etUsername.setTextCursorDrawable(R.drawable.cursor_white);
            binding.etEmail.setTextCursorDrawable(R.drawable.cursor_white);
            binding.etPassword.setTextCursorDrawable(R.drawable.cursor_white);
            binding.etVerifyPassword.setTextCursorDrawable(R.drawable.cursor_white);
        }


        binding.textInputLayout.setEndIconOnClickListener(v -> {
            binding.birthdateEditText.setText("");
            binding.birthdateEditText.setError(null);
        });

        binding.clearProfilePictureButton.setOnClickListener(v -> binding.ivProfilePicture.setImageResource(R.drawable.default_profile_picture));

        binding.ivProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    registerUser();
                }
            }
        });

        binding.birthdateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.birthdateEditText.setError(null);
                showDatePickerDialog();
            }
        });

    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            binding.ivProfilePicture.setImageURI(imageUri);
        }
    }


    private void showDatePickerDialog() {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Set the selected birthdate to the EditText
                    String birthdate = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year1;
                    binding.birthdateEditText.setText(birthdate);
                }, year, month, day);

        datePickerDialog.show();
    }

    private boolean validateFields() {
        boolean isValid = true;

        String fullName = binding.etFullName.getText().toString().trim();
        String username = binding.etUsername.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String birthdate = binding.birthdateEditText.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String verifyPassword = binding.etVerifyPassword.getText().toString().trim();

        // Validate Full Name
        if (fullName.isEmpty()) {
            binding.etFullName.setError("Full Name is required");
            isValid = false;
        }

        // Validate Username
        if (username.isEmpty()) {
            binding.etUsername.setError("Username is required");
            isValid = false;
        }

        // Validate Email
        if (email.isEmpty()) {
            binding.etEmail.setError("Email is required");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.setError("Invalid email format");
            isValid = false;
        }

        // Validate Birthdate
        if (!birthdate.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            sdf.setLenient(false);

            try {
                Date birthdateDate = sdf.parse(birthdate);
                Date minDate = sdf.parse("01.01.1905");
                Date today = new Date();

                if (birthdateDate.before(minDate) || birthdateDate.after(today)) {
                    binding.birthdateEditText.setError("Birthdate must be between 01.01.1905 and today");
                    isValid = false;
                }


            } catch (ParseException e) {
                binding.birthdateEditText.setError("Invalid birthdate format (use dd.MM.yyyy)");
                isValid = false;
            }
        }

        // Validate Password
        if (password.isEmpty()) {
            binding.etPassword.setError("Password is required");
            isValid = false;
        } else if (password.length() < 8) {
            binding.etPassword.setError("Password must be at least 8 characters long");
            isValid = false;
        }

        // Validate Verify Password
        if (verifyPassword.isEmpty()) {
            binding.etVerifyPassword.setError("Please confirm your password");
            isValid = false;
        } else if (!verifyPassword.equals(password)) {
            binding.etVerifyPassword.setError("Passwords do not match");
            isValid = false;
        }
        return isValid;
    }

    private void registerUser() {
        String fullName = binding.etFullName.getText().toString().trim();
        String username = binding.etUsername.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String birthdate = binding.birthdateEditText.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String verifyPassword = binding.etVerifyPassword.getText().toString().trim();
        String photoName = "example.jpg";
        String photoUrl = "https://example.com/photo.jpg";

        User user = new User(fullName, username, email, birthdate, password, photoName, photoUrl);

        Toast.makeText(RegisterActivity.this, "finished setting", Toast.LENGTH_SHORT).show();

        ApiService apiService = RetrofitClient.getApiService();
        Call<AuthResponse> call = apiService.registerUser(user);

        Toast.makeText(RegisterActivity.this, "before sending", Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                Toast.makeText(RegisterActivity.this, "inside on response", Toast.LENGTH_SHORT).show();

                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    if (token != null) {
                        saveToken(token);
                        Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        // Navigate to HomeActivity if needed
                        // startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error: " + response.body().getErrors(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("jwtToken", token);
        editor.apply();
    }

}