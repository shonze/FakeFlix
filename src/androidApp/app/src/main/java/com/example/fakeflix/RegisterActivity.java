package com.example.fakeflix;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import androidx.core.content.FileProvider;
import com.example.fakeflix.databinding.ActivityRegisterBinding;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;
import androidx.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONObject;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.room.Room;


public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;

    private static final int PICK_IMAGE = 100;
    private static final int TAKE_PHOTO = 101;
    private Uri imageUri;

    private UserDB db;
    private UserDetails userDetails;
    UserDetailsDao userDetailsDao;

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

        binding.clearProfilePictureButton.setOnClickListener(v -> {
            binding.ivProfilePicture.setImageResource(R.drawable.default_profile_picture);
            imageUri = null;
        });

        binding.ivProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a dialog to choose between taking a photo or selecting from the gallery
                CharSequence[] options = {"Take Photo", "Choose from Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Select an Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            // Open the camera to take a photo
                            openCamera();
                        } else {
                            // Open the gallery to choose an image
                            openGallery();
                        }
                    }
                });
                builder.show();
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    checkUser();
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

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Create a temporary file to store the photo
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(RegisterActivity.this,
                        "com.example.fakeflix.fileprovider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE && data != null) {
                imageUri = data.getData();
                binding.ivProfilePicture.setImageURI(imageUri);
            } else if (requestCode == TAKE_PHOTO) {
                binding.ivProfilePicture.setImageURI(imageUri);
            }
        }
    }

    // Method to create a temporary file for the camera photo
    private File createImageFile() throws IOException {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
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
                    String birthdate = (monthOfYear + 1) + "." + dayOfMonth + "." + year1;
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
    private void checkUser() {
        String fullName = binding.etFullName.getText().toString().trim();
        String username = binding.etUsername.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String birthdate = binding.birthdateEditText.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String photoName = "noPic.png";
        String photoUrl = "http://localhost:8080/uploads/noPic.png";

        User user = new User(fullName, username, email, birthdate, password, photoName, photoUrl);

        ApiService apiService = RetrofitClient.getApiService();
        Call<ResponseBody> call = apiService.checkUser(user);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegisterActivity.this, "passed check user: " , Toast.LENGTH_SHORT).show();
                    // Handle successful response here
                    registerUser(fullName, username, email, birthdate, password,photoName, photoUrl);
                } else {
                    Toast.makeText(RegisterActivity.this, "Error: " + response.body(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void registerUser(String fullName,String username,String email,String birthdate,
                              String password, String photoName, String photoUrl ) {

        if (imageUri != null) {
            String[] arr = {photoUrl, photoName};  // Array initialization
            // Ensure the values are not modified inside uploadPhoto if you don't want to change them
            uploadPhoto(arr);
            photoUrl = arr[0];  // Update the original values from the array
            photoName = arr[1];
        }
        String copyPhotoUrl = photoUrl;
        User user = new User(fullName, username, email, birthdate, password, photoName, photoUrl);

        ApiService apiService = RetrofitClient.getApiService();
        Call<AuthResponse> call = apiService.registerUser(user);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    if (token != null) {
                        saveToken(token);
                        Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();

                        db = Room.databaseBuilder(getApplicationContext(),
                                        UserDB.class, "UserDB")
                                .allowMainThreadQueries().build();
                        userDetailsDao = db.userDetailsDao();
                        handleSave(fullName, copyPhotoUrl);

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

    private void uploadPhoto(String[] arr) {
        MultipartBody.Part imagePart = prepareFilePart(imageUri);

        ApiService apiService = RetrofitClient.getApiService();
        Call<ResponseBody> call = apiService.uploadImage(imagePart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {

                    try {
                        // You may need to parse the response if it's in JSON format
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        arr[0] = jsonResponse.getString("url");
                        arr[1] = jsonResponse.getString("name");

                    } catch (Exception e) {
                        Log.e("Upload", "Error parsing response: " + e.getMessage());
                    }

                    Log.d("Upload", "Success!");
                } else {
                    Log.e("Upload", "Failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload", "Error: " + t.getMessage());
            }
        });
    }

    private File getFileFromUri(Uri uri) {
        File file = null;
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            file = new File(getCacheDir(), "upload_image.jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

//    private MultipartBody.Part prepareFilePart(Uri uri) {
//        File file = getFileFromUri(uri);
//        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
//        return MultipartBody.Part.createFormData("files", file.getName(), requestFile);
//    }

    private MultipartBody.Part prepareFilePart(Uri uri) {
        File file = getFileFromUri(uri);

        // Get MIME type from ContentResolver
        String mimeType = getContentResolver().getType(uri);
        if (mimeType == null) {
            mimeType = "image/jpg"; // Default MIME type
        }

        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
        return MultipartBody.Part.createFormData("files", file.getName(), requestFile);
    }


    private void handleSave(String fullName, String photoUrl) {
        if (userDetails == null) {
            userDetails = new UserDetails(fullName, photoUrl);
            userDetailsDao.insert(userDetails);
        }
        else {
            userDetails.setUserFullName(fullName);
            userDetails.setUserPhotoUrl(photoUrl);
            userDetailsDao.update(userDetails);
        }
        finish();
    }
}