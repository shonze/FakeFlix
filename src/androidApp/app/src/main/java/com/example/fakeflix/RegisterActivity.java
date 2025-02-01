package com.example.fakeflix;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;

import com.example.fakeflix.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Calendar;
import java.util.Locale;

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
            binding.birthdateEditText.setText("");  // Clear the text programmatically if needed
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
                registerUser();
            }
        });

        binding.birthdateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private void registerUser() {
        String fullName = binding.etFullName.getText().toString().trim();
        String username = binding.etUsername.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String birthday = binding.birthdateEditText.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String verifyPassword = binding.etVerifyPassword.getText().toString().trim();

        // TODO: Implement user registration logic here
        // This is where you would typically send the data to your backend or authentication service

        // For now, we'll just show a toast message
        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
    }

}