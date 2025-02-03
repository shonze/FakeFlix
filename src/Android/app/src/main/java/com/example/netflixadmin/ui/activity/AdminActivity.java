package com.example.netflixadmin.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {
    private static final int PICK_VIDEO_REQUEST = 1;
    private static final int PICK_IMAGE_REQUEST = 2;
    private CategoryViewModel categoryViewModel;
    private MovieViewModel movieViewModel;
    private CategoryAdapter categoryAdapter;
    private MovieAdapter movieAdapter;

    private Button btnAddCategory;
    private Button btnAddMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        RecyclerView categoryRecyclerView = findViewById(R.id.recyclerViewCategories);
        RecyclerView movieRecyclerView = findViewById(R.id.recyclerViewMovies); // Assuming you have this RecyclerView
        btnAddCategory = findViewById(R.id.btnAddCategory);
        btnAddMovie = findViewById(R.id.btnAddMovie);
        SearchView searchViewMovies = findViewById(R.id.searchViewMovies);

        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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
        movieRecyclerView.setAdapter(movieAdapter);

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
        btnAddMovie.setOnClickListener(v -> showAddMovieDialog());

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
    }

    private void showAddCategoryDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_category);

        EditText edtCategoryName = dialog.findViewById(R.id.edtCategoryName);
        Button btnSaveCategory = dialog.findViewById(R.id.btnSaveCategory);

        btnSaveCategory.setOnClickListener(v -> {
            String categoryName = edtCategoryName.getText().toString().trim();
            if (categoryName.isEmpty()) {
                Toast.makeText(this, "Category name is required", Toast.LENGTH_SHORT).show();
                return;
            }

            CategoryEntity newCategory = new CategoryEntity();
            newCategory.setName(categoryName);
            categoryViewModel.addCategory(newCategory, new Callback<CategoryEntity>() {
                @Override
                public void onResponse(Call<CategoryEntity> call, Response<CategoryEntity> response) {
                    if (response.isSuccessful()) {
                        categoryViewModel.fetchCategories(); // Refresh categories
                        Toast.makeText(AdminActivity.this, "Category added", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(AdminActivity.this, "Failed to add category", Toast.LENGTH_SHORT).show();
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
        Button btnUpdateCategory = dialog.findViewById(R.id.btnUpdateCategory);

        edtCategoryName.setText(category.getName());

        btnUpdateCategory.setOnClickListener(v -> {
            category.setName(edtCategoryName.getText().toString().trim());
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
    }

    private void showAddMovieDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_movie, null);

        // Get references to EditText fields
        EditText movieTitleEditText = dialogView.findViewById(R.id.edtMovieTitle);
        EditText movieDescriptionEditText = dialogView.findViewById(R.id.edtMovieDescription);
        EditText movieLengthEditText = dialogView.findViewById(R.id.edtMovieLength);

        // Get references to MaterialButton fields
        MaterialButton btnSelectVideo = dialogView.findViewById(R.id.btnMovieSelectVideo);
        MaterialButton btnSelectThumbnail = dialogView.findViewById(R.id.btnMovieSelectThumbnail);
        MaterialButton btnSave = dialogView.findViewById(R.id.btnMovieSave);

        // Create the dialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add Movie")
                .setView(dialogView)
                .create();

        btnSelectVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_VIDEO_REQUEST);
            }
        });
        btnSelectThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        btnSave.setOnClickListener(v -> {
            String movieTitle = movieTitleEditText.getText().toString().trim();
            String movieDescription = movieDescriptionEditText.getText().toString().trim();
            String movieLengthStr = movieLengthEditText.getText().toString().trim();
            String movieThumbnail = btnSelectThumbnail.getText().toString().trim();
            String movieVideo= btnSelectVideo.getText().toString().trim();

            if (movieTitle.isEmpty() || movieDescription.isEmpty() || movieLengthStr.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            int movieLength = Integer.parseInt(movieLengthStr);

            // Create a new MovieEntity
            MovieEntity newMovie = new MovieEntity();
            newMovie.setTitle(movieTitle);
            newMovie.setDescription(movieDescription);
            newMovie.setLength(movieLength);
            newMovie.setThumbnail(movieThumbnail);
            newMovie.setVideoUrl(movieVideo);

            // Call ViewModel to add movie
            movieViewModel.addMovie(newMovie, new Callback<MovieEntity>() {
                @Override
                public void onResponse(Call<MovieEntity> call, Response<MovieEntity> response) {
                    if (response.isSuccessful()) {
                        movieViewModel.fetchMovies(); // Refresh movie list
                        Toast.makeText(AdminActivity.this, "Movie added", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(AdminActivity.this, "Failed to add movie", Toast.LENGTH_SHORT).show();
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
        edtMovieThumbnail.setText(movie.getThumbnail());
        edtMovieVideo.setText(movie.getVideoUrl());

        btnUpdateMovie.setOnClickListener(v -> {
            movie.setTitle(edtMovieTitle.getText().toString().trim());
            movie.setDescription(edtMovieDescription.getText().toString().trim());
            movie.setLength(Integer.parseInt(edtMovieLength.getText().toString().trim()));
            movie.setThumbnail(edtMovieThumbnail.getText().toString().trim());
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
