package com.example.fakeflix;

import android.os.Build;
import android.os.Bundle;

import com.example.fakeflix.databinding.ActivityRegisterBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.fakeflix.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

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

        // TODO: Implement user registration logic here
        // This is where you would typically send the data to your backend or authentication service

        // For now, we'll just show a toast message
        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
    }
}