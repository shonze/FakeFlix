package com.example.netflixadmin.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflixadmin.R;
import com.example.netflixadmin.data.local.CategoryEntity;
import com.example.netflixadmin.data.local.MovieEntity;
import com.example.netflixadmin.ui.adapter.CategoryAdapter;
import com.example.netflixadmin.ui.adapter.MovieAdapter;
import com.example.netflixadmin.ui.viewmodel.CategoryViewModel;
import com.example.netflixadmin.ui.viewmodel.MovieViewModel;
import com.example.netflixadmin.utils.LiveDataUtils;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> videoPickerLauncher;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri selectedVideoUri;
    private Uri selectedImageUri;
    private MaterialButton btnSelectVideo;
    private MaterialButton btnSelectThumbnail;
    private String savedTitle = "";
    private String savedDescription = "";
    private String savedLength = "";
    private List<String> savedSelectedCategories = new ArrayList<>();

    private static final int PICK_VIDEO_REQUEST = 1;
    private static final int PICK_IMAGE_REQUEST = 2;
    private CategoryViewModel categoryViewModel;
    private MovieViewModel movieViewModel;
    private CategoryAdapter categoryAdapter;
    private MovieAdapter movieAdapter;

    private Button btnAddCategory;
    private Button btnAddMovie;
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (selectedVideoUri != null) {
            outState.putString("selectedVideoUri", selectedVideoUri.toString());
        }
        if (selectedImageUri != null) {
            outState.putString("selectedImageUri", selectedImageUri.toString());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        RecyclerView categoryRecyclerView = findViewById(R.id.recyclerViewCategories);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        btnAddMovie = findViewById(R.id.btnAddMovie);
        SearchView searchViewMovies = findViewById(R.id.searchViewMovies);

        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        categoryAdapter = new CategoryAdapter(null, new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(CategoryEntity category) {
                showEditCategoryDialog(category);
            }

            @Override
            public void onDeleteClick(CategoryEntity category) {
                showDeleteCategoryDialog(category);
            }
        });
        categoryRecyclerView.setAdapter(categoryAdapter);

        movieAdapter = new MovieAdapter(null, new MovieAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(MovieEntity movie) {
                showEditMovieDialog(movie);
            }

            @Override
            public void onDeleteClick(MovieEntity movie) {
                showDeleteMovieDialog(movie);
            }
        });

        // Observe category updates
        categoryViewModel.categoriesLiveData.observe(this, new Observer<List<CategoryEntity>>() {
            @Override
            public void onChanged(List<CategoryEntity> categories) {
                categoryAdapter.setCategories(categories);
            }
        });

        // Observe movie updates
        movieViewModel.moviesLiveData.observe(this, new Observer<List<MovieEntity>>() {
            @Override
            public void onChanged(List<MovieEntity> movies) {
                movieAdapter.setMovies(movies);
            }
        });

        categoryViewModel.fetchCategories();
        movieViewModel.fetchMovies(); // Fetch movies

        btnAddCategory.setOnClickListener(v -> showAddCategoryDialog());

        List<String> categoryNames = new ArrayList<>();
        movieViewModel.getCategories().observe(this, categories -> {
                    for (CategoryEntity category : categories) {
                        categoryNames.add(category.getName());
                    }
        });

        btnAddMovie.setOnClickListener(v -> {
                showAddMovieDialog(categoryNames);
        });

        // Setup search view listener
        searchViewMovies.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String id) {
                movieViewModel.getMovieById(id); // You need to implement this in your MovieViewModel
                return true;
            }
        });
        videoPickerLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri videoUri = result.getData().getData();
                        Log.e("THE URI:", videoUri.toString());
                        if (videoUri != null) {
                            movieViewModel.uploadVideo(videoUri, this);  // <-- Update ViewModel
                            btnSelectVideo.setText("Video Selected");
                        }
                    }
                });

        imagePickerLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        Log.e("THE URI:", imageUri.toString());
                        if (imageUri != null) {
                            movieViewModel.uploadThumbnail(imageUri, this);  // Store in ViewModel
                            btnSelectThumbnail.setText("Thumbnail Selected");
                        }
                    }
                });

    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == PICK_IMAGE_REQUEST && data != null) {
//                selectedThumbnailUri = data.getData().toString();
//            } else if (requestCode == PICK_VIDEO_REQUEST && data != null)  {
//                selectedVideoUri = data.getData().toString();
//                btnSelectVideo.setText(selectedVideoUri);
//            }
//        }
//    }

    private void showAddCategoryDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_category);

        EditText edtCategoryDesc = dialog.findViewById(R.id.edtCategoryDesc);
        EditText edtCategoryName = dialog.findViewById(R.id.edtCategoryName);
        CheckBox checkboxPromoted = dialog.findViewById(R.id.checkboxPromoted);
        Button btnSaveCategory = dialog.findViewById(R.id.buttonAddCategory);

        btnSaveCategory.setOnClickListener(v -> {
            String categoryName = edtCategoryName.getText().toString().trim();
            String categoryDesc = edtCategoryDesc.getText().toString().trim();
            boolean isPromoted = checkboxPromoted.isChecked(); // Get the state of the checkbox

            if (categoryName.isEmpty() || categoryDesc.isEmpty()) {
                Toast.makeText(this, "Category name and/or Description is required", Toast.LENGTH_SHORT).show();
                return;
            }

            CategoryEntity newCategory = new CategoryEntity();
            newCategory.setName(categoryName);
            newCategory.setDescription(categoryDesc);
            newCategory.setPromoted(isPromoted); // Set the promoted attribute

            categoryViewModel.addCategory(newCategory, new Callback<CategoryEntity>() {
                @Override
                public void onResponse(Call<CategoryEntity> call, Response<CategoryEntity> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(AdminActivity.this, "Category added", Toast.LENGTH_SHORT).show();
                        // Fetch categories after adding to update the list
                        categoryViewModel.fetchCategories(); // Refresh categories
                        dialog.dismiss();
                    } else {
                        Toast.makeText(AdminActivity.this, "Failed to add category", Toast.LENGTH_SHORT).show();
                        System.out.println("AddCategory Response code: " + response.code());
                        try {
                            String errorBody = response.errorBody().toString();
                            System.out.println(response);
                            Toast.makeText(AdminActivity.this, "Failed to add category: " + errorBody, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CategoryEntity> call, Throwable t) {
                    Toast.makeText(AdminActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        dialog.show();
    }



    private void showEditCategoryDialog(CategoryEntity category) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_category);

        EditText edtCategoryName = dialog.findViewById(R.id.edtCategoryName);
        EditText edtCategoryDesc = dialog.findViewById(R.id.edtCategoryDesc); // New EditText for description
        CheckBox checkboxPromoted = dialog.findViewById(R.id.checkboxPromoted); // New CheckBox for promoted
        Button btnUpdateCategory = dialog.findViewById(R.id.btnUpdateCategory);

        // Set current category values
        edtCategoryName.setText(category.getName());
        edtCategoryDesc.setText(category.getDescription()); // Set the current description
        checkboxPromoted.setChecked(category.isPromoted()); // Set the promoted state

        btnUpdateCategory.setOnClickListener(v -> {
            // Get updated values from the dialog
            category.setName(edtCategoryName.getText().toString().trim());
            category.setDescription(edtCategoryDesc.getText().toString().trim()); // Update description
            category.setPromoted(checkboxPromoted.isChecked()); // Update promoted state

            categoryViewModel.updateCategory(category.getCategoryId(), category, new Callback<CategoryEntity>() {
                @Override
                public void onResponse(Call<CategoryEntity> call, Response<CategoryEntity> response) {
                    if (response.isSuccessful()) {
                        categoryViewModel.fetchCategories(); // Refresh categories
                        Toast.makeText(AdminActivity.this, "Category updated", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(AdminActivity.this, "Failed to update category", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CategoryEntity> call, Throwable t) {
                    Toast.makeText(AdminActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }


    private void showDeleteCategoryDialog(CategoryEntity category) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Category")
                .setMessage("Are you sure you want to delete " + category.getName() + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    categoryViewModel.deleteCategory(category.getCategoryId(), new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                categoryViewModel.fetchCategories(); // Refresh categories
                                Toast.makeText(AdminActivity.this, "Category deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AdminActivity.this, "Failed to delete category", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(AdminActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("No", null)
                .show();
        categoryViewModel.fetchCategories();
    }

    private Dialog addMovieDialog; // Class-level variable

    private void showAddMovieDialog(List<String> categoryNames) {
        if (categoryNames == null || categoryNames.isEmpty()) {
            Toast.makeText(this, "Categories are still loading...", Toast.LENGTH_SHORT).show();
            return; // Prevent dialog from opening until categories are ready.
        }
        // Prevent multiple dialogs
        if (addMovieDialog != null && addMovieDialog.isShowing()) {
            return;
        }

        // Initialize the dialog
        addMovieDialog = new Dialog(this);
        addMovieDialog.setContentView(R.layout.dialog_add_movie);
        addMovieDialog.setCancelable(false);
        addMovieDialog.getWindow().setDimAmount(0f);

        // Find the close button
        Button closeButton = addMovieDialog.findViewById(R.id.close_dialog_button);

        // Set OnClickListener for the close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the addMovieDialog when the button is clicked
                addMovieDialog.dismiss();
            }
        });

        // Get references to EditText fields
        EditText movieTitleEditText = addMovieDialog.findViewById(R.id.edtMovieTitle);
        EditText movieDescriptionEditText = addMovieDialog.findViewById(R.id.edtMovieDescription);
        EditText movieLengthEditText = addMovieDialog.findViewById(R.id.edtMovieLength);

        // Restore previously saved values
        movieTitleEditText.setText(savedTitle);
        movieDescriptionEditText.setText(savedDescription);
        movieLengthEditText.setText(savedLength);

        // Get references to buttons
        btnSelectVideo = addMovieDialog.findViewById(R.id.btnMovieSelectVideo);
        btnSelectThumbnail = addMovieDialog.findViewById(R.id.btnMovieSelectThumbnail);
        MaterialButton btnSave = addMovieDialog.findViewById(R.id.btnMovieSave);
        Button btnSelectCategories = addMovieDialog.findViewById(R.id.btnMovieSelectCategories);

        boolean[] selectedCategories = new boolean[categoryNames.size()];
        List<String> selectedCategoryList = new ArrayList<>(savedSelectedCategories); // Restore saved categories

        btnSelectCategories.setOnClickListener(v -> {
            AlertDialog.Builder categoryDialog = new AlertDialog.Builder(addMovieDialog.getContext()); // Use the correct context
            categoryDialog.setTitle("Select Categories");

            String[] categoryNamesArray = categoryNames.toArray(new String[0]); // Convert List to Array
//            Log.d("Categories", categoryNamesArray[1]+ categoryNamesArray[9]);
            for (int i = 0; i < categoryNamesArray.length; i++) {
                if (selectedCategoryList.contains(categoryNamesArray[i])) {
                    selectedCategories[i] = true;
                }
            }

            categoryDialog.setMultiChoiceItems(categoryNamesArray, selectedCategories, (dialog1, which, isChecked) -> {
                if (isChecked) selectedCategoryList.add(categoryNamesArray[which]);
                else selectedCategoryList.remove(categoryNamesArray[which]);
            });

            categoryDialog.setPositiveButton("OK", (dialog12, which) -> {
                savedSelectedCategories = new ArrayList<>(selectedCategoryList);
                Toast.makeText(addMovieDialog.getContext(), "Selected Categories: " + savedSelectedCategories, Toast.LENGTH_SHORT).show();
            });

            categoryDialog.setNegativeButton("Cancel", (dialog1, which) -> dialog1.dismiss());

            // Show the dialog
            AlertDialog dialog = categoryDialog.create();
            dialog.show();
        });

        btnSelectVideo.setOnClickListener(v -> {
            saveCurrentInputs(movieTitleEditText, movieDescriptionEditText, movieLengthEditText, selectedCategoryList);
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            videoPickerLauncher.launch(intent);
        });

        btnSelectThumbnail.setOnClickListener(v -> {
            saveCurrentInputs(movieTitleEditText, movieDescriptionEditText, movieLengthEditText, selectedCategoryList);
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        btnSave.setOnClickListener(v -> {
            btnSave.setEnabled(false);
            String movieTitle = movieTitleEditText.getText().toString().trim();
            String movieDescription = movieDescriptionEditText.getText().toString().trim();
            String movieLengthStr = movieLengthEditText.getText().toString().trim();

            if (movieTitle.isEmpty() || movieDescription.isEmpty() || movieLengthStr.isEmpty() || selectedCategoryList.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                btnSave.setEnabled(true); // Re-enable button if validation fails
                return;
            }

            if (movieViewModel.getThumbnailUrl().getValue() == null) {
                Toast.makeText(this, "Please select a thumbnail", Toast.LENGTH_SHORT).show();
                btnSave.setEnabled(true); // Re-enable button if validation fails
                return;
            }

            if (movieViewModel.getVideoUrl().getValue() == null) {
                Toast.makeText(this, "Please select a video", Toast.LENGTH_SHORT).show();
                btnSave.setEnabled(true); // Re-enable button if validation fails
                return;
            }

            int movieLength = Integer.parseInt(movieLengthStr);

            // Start upload process
            movieViewModel.uploadThumbnail(Uri.parse(movieViewModel.getThumbnailUrl().getValue().first), this);
            movieViewModel.uploadVideo(Uri.parse(movieViewModel.getVideoUrl().getValue().first), this);

            // Observe uploads and wait until both URLs are available
            LiveDataUtils.observeOnce(movieViewModel.getThumbnailUrl(), this, thumbnailUrl -> {
                if (thumbnailUrl != null) {
                    Log.d("ShowAddMovieDialog", "Thumbnail URL obtained: " + thumbnailUrl.first); // Debugging
                    LiveDataUtils.observeOnce(movieViewModel.getVideoUrl(), this, videoUrl -> {
                        if (videoUrl != null) {
                            Log.d("ShowAddMovieDialog", "Video URL obtained: " + videoUrl.first); // Debugging
                            MovieEntity newMovie = new MovieEntity();
                            newMovie.setTitle(movieTitle);
                            newMovie.setDescription(movieDescription);
                            newMovie.setLength(movieLength);
                            newMovie.setThumbnail(thumbnailUrl.first);
                            newMovie.setVideoUrl(videoUrl.first);
                            newMovie.setCategories(selectedCategoryList);
                            newMovie.setThumbnailName(thumbnailUrl.second);
                            newMovie.setVideoName(videoUrl.second);

                            // Call ViewModel function with a callback
                            movieViewModel.addMovie(newMovie, new Callback<MovieEntity>() {
                                @Override
                                public void onResponse(Call<MovieEntity> call, Response<MovieEntity> response) {
                                    if (response.isSuccessful()) {
                                        Log.d("ShowAddMovieDialog", "Movie added successfully"); // Debugging
                                        Toast.makeText(AdminActivity.this, "Movie added successfully!", Toast.LENGTH_SHORT).show();
                                        try {
                                            addMovieDialog.dismiss();
                                        } catch (Exception e) {
                                            Log.e("DialogDismiss", "Error dismissing dialog: " + e.getMessage());
                                        }
                                    } else {
                                        Log.e("AddMovie", "Failed to add movie: " + response.code() + " " + response.message());
                                        Toast.makeText(AdminActivity.this, "Failed to add movie. Response code: " + response.code(), Toast.LENGTH_SHORT).show();
                                        try {
                                            addMovieDialog.dismiss();
                                        } catch (Exception e) {
                                            Log.e("DialogDismiss", "Error dismissing dialog: " + e.getMessage());
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MovieEntity> call, Throwable t) {
                                    Log.e("ShowAddMovieDialog", "Error occurred: " + t.getMessage()); // Debugging
                                    Toast.makeText(AdminActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                                    addMovieDialog.dismiss(); // Dismiss the addMovieDialog on failure
                                }
                            });
                        } else {
                            Log.e("ShowAddMovieDialog", "Failed to obtain video URL"); // Debugging
                            addMovieDialog.dismiss(); // Dismiss if video upload fails
                            Toast.makeText(AdminActivity.this, "Video upload failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.e("ShowAddMovieDialog", "Failed to obtain thumbnail URL"); // Debugging
                    addMovieDialog.dismiss(); // Dismiss if thumbnail upload fails
                    Toast.makeText(AdminActivity.this, "Thumbnail upload failed.", Toast.LENGTH_SHORT).show();
                }
            });

            // Removed delayed enabling of the button for better UX
        });

        addMovieDialog.setCancelable(true); // Allow the addMovieDialog to be canceled
        addMovieDialog.show(); // Show the dialog
    }






    // Helper method to save input values before opening video/image picker
    private void saveCurrentInputs(EditText title, EditText description, EditText length, List<String> categories) {
        savedTitle = title.getText().toString();
        savedDescription = description.getText().toString();
        savedLength = length.getText().toString();
        savedSelectedCategories = new ArrayList<>(categories);
    }


    private String getCategoryIdByName(String name, List<String[]> categoryNamesIds) {
        for (String[] ar : categoryNamesIds) {
            if (ar[0].equals(name)) {
                return ar[1];
            }
        }
        return "ERROR";
    }
    private void showEditMovieDialog(MovieEntity movie) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_movie);

        EditText edtMovieTitle = dialog.findViewById(R.id.edtMovieTitle);
        EditText edtMovieDescription = dialog.findViewById(R.id.edtMovieDescription);
        EditText edtMovieLength = dialog.findViewById(R.id.edtMovieLength);
        EditText edtMovieThumbnail = dialog.findViewById(R.id.btnSelectThumbnail); // Assuming thumbnail URL input
        EditText edtMovieVideo = dialog.findViewById(R.id.btnSelectVideo);
        Button btnUpdateMovie = dialog.findViewById(R.id.btnUpdateMovie);


        edtMovieTitle.setText(movie.getTitle());
        edtMovieDescription.setText(movie.getDescription());
        edtMovieLength.setText(String.valueOf(movie.getLength()));
        edtMovieThumbnail.setText(movie.getThumbnailName());
        edtMovieVideo.setText(movie.getVideoUrl());

        btnUpdateMovie.setOnClickListener(v -> {
            movie.setTitle(edtMovieTitle.getText().toString().trim());
            movie.setDescription(edtMovieDescription.getText().toString().trim());
            movie.setLength(Integer.parseInt(edtMovieLength.getText().toString().trim()));
            movie.setThumbnailName(edtMovieThumbnail.getText().toString().trim());
            movie.setVideoUrl(edtMovieVideo.getText().toString().trim());

            movieViewModel.updateMovie(movie, new Callback<MovieEntity>() {
                @Override
                public void onResponse(Call<MovieEntity> call, Response<MovieEntity> response) {
                    if (response.isSuccessful()) {
                        movieViewModel.fetchMovies(); // Refresh movie list
                        Toast.makeText(AdminActivity.this, "Movie updated", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(AdminActivity.this, "Failed to update movie", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MovieEntity> call, Throwable t) {
                    Toast.makeText(AdminActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }

    private void showDeleteMovieDialog(MovieEntity movie) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Movie")
                .setMessage("Are you sure you want to delete " + movie.getTitle() + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    movieViewModel.deleteMovie(movie.getId(), new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                movieViewModel.fetchMovies(); // Refresh movie list
                                Toast.makeText(AdminActivity.this, "Movie deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AdminActivity.this, "Failed to delete movie", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(AdminActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("No", null)
                .show();
    }
}
