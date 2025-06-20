package com.example.advanced_programing_ex_4;

import static android.view.View.VISIBLE;
import static com.example.advanced_programing_ex_4.MyApplication.context;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.AppCompatImageButton;

import com.bumptech.glide.Glide;
import com.example.advanced_programing_ex_4.Adapters.CategoryListAdapter;
import com.example.advanced_programing_ex_4.Adapters.MoviesListsAdapter;
import com.example.advanced_programing_ex_4.Factories.CategoryViewModelFactory;
import com.example.advanced_programing_ex_4.Factories.MoviesListsViewModelFactory;
import com.example.advanced_programing_ex_4.Factories.MoviesViewModelFactory;
import com.example.advanced_programing_ex_4.View_Models.CategoriesViewModel;
import com.example.advanced_programing_ex_4.View_Models.MoviesListsViewModel;
import com.example.advanced_programing_ex_4.View_Models.MoviesViewModel;
import com.example.advanced_programing_ex_4.entities.Movie;
import com.example.advanced_programing_ex_4.entities.MoviesList;
import com.example.fakeflix.ApiService;
import com.example.fakeflix.R;
import com.example.fakeflix.RetrofitClient;
import com.example.fakeflix.SearchActivity;
import com.example.fakeflix.StartingActivity;
import com.example.fakeflix.VideoPlayerActivity;
import com.example.fakeflix.WatchMovieActivity;
import com.example.fakeflix.ui.activity.AdminActivity;
import com.example.fakeflix.utils.Constants;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private NestedScrollView scrollView;
    private DrawerLayout drawer;
    private MoviesListsViewModel moviesListsViewModel;
    private MoviesViewModel moviesViewModel;
    private CategoriesViewModel categoriesViewModel;
    private SharedPreferences preferences;
    private String jwtToken;
    private boolean isAdmin;
    private Movie randomMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get from the intent if the user is admin
        preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        if (preferences.getString("isAdmin", "false").equals("true"))
            isAdmin = true;
        else
            isAdmin = false;

        // Get the user token
        preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        jwtToken = preferences.getString("jwtToken", null);

        // Initialize Views Safely
        toolbar = findViewById(R.id.toolbar);
        scrollView = findViewById(R.id.nestedScrollView);
        @SuppressLint("WrongViewCast") AppCompatImageButton searchButton = findViewById(R.id.search_button);
        @SuppressLint("WrongViewCast") AppCompatImageButton menuButton = findViewById(R.id.menuButton);
        RecyclerView lstMoviesLists = findViewById(R.id.movie_lists_recycler_view);
        TextView topMovieTitleTextView = findViewById(R.id.top_movie_title);
        TextView topMovieDescription = findViewById(R.id.top_movie_description);
        ImageView topMovieThumbnail = findViewById(R.id.top_movie_image);
        Button topMoviePlayButton = findViewById(R.id.top_movie_play_button);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();

        MenuItem adminMenuItem = menu.findItem(R.id.admin_screen);

        // Check if the user is an admin
        if (isAdmin) {
            // Show the admin menu item
            adminMenuItem.setVisible(true);
        } else {
            // Hide the admin menu item
            adminMenuItem.setVisible(false);
        }


        setSupportActionBar(toolbar);

        // Set up the ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.play, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        // Ensure buttons exist before setting listeners
        if (searchButton != null) {
            searchButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);

                startActivity(intent);
            });
        }

        if (topMoviePlayButton != null) {
            topMoviePlayButton.setOnClickListener(v -> {
                if (randomMovie != null) {
                    Intent intent = new Intent(MainActivity.this, WatchMovieActivity.class);

                    intent.putExtra("movieId", randomMovie.getMovieId());
                    intent.putExtra("movieTitle", randomMovie.getTitle());
                    intent.putExtra("movieThumbnail", Constants.BASE_URL + "/uploads/" + randomMovie.getThumbnailName());
                    intent.putExtra("movieVideo", Constants.BASE_URL + "/uploads/" + randomMovie.getVideoName());
                    intent.putExtra("movieDescription", randomMovie.getDescription());
                    intent.putExtra("movieLength", randomMovie.getLength());
                    intent.putExtra("movieCategories", randomMovie.getCategories().toArray(new String[0]));

                    startActivity(intent);
                }
            });
        }

        if (menuButton != null) {
            menuButton.setOnClickListener(v -> drawer.openDrawer(GravityCompat.START));
        }

        // Ensure RecyclerView is not null
        if (lstMoviesLists != null) {
            MoviesListsAdapter moviesListsAdapter = new MoviesListsAdapter(this);
            lstMoviesLists.setAdapter(moviesListsAdapter);
            lstMoviesLists.setLayoutManager(new LinearLayoutManager(this));

            // ViewModel Initialization
            moviesListsViewModel = new ViewModelProvider(this, new MoviesListsViewModelFactory(this, jwtToken)).get(MoviesListsViewModel.class);
            moviesViewModel = new ViewModelProvider(this, new MoviesViewModelFactory(this, jwtToken)).get(MoviesViewModel.class);
            categoriesViewModel = new ViewModelProvider(this, new CategoryViewModelFactory(this, jwtToken)).get(CategoriesViewModel.class);

            // Observe ViewModel Data Safely
            moviesListsViewModel.get().observe(this, moviesLists -> {
                if (moviesLists != null) {
                    moviesListsAdapter.setMoviesLists(moviesLists);

                    Set<String> uniqueMovieIds = new HashSet<>();
                    for (MoviesList item : moviesLists) {
                        if (item != null && item.getMovieIds() != null) {
                            uniqueMovieIds.addAll(item.getMovieIds());
                        }
                    }

                    if (!uniqueMovieIds.isEmpty()) {
                        randomMovie = moviesViewModel.getRandomMovie(new ArrayList<>(uniqueMovieIds));

                        if (randomMovie != null) {
                            if (topMovieTitleTextView != null) {
                                topMovieTitleTextView.setText(randomMovie.getTitle());
                            }
                            if (topMovieDescription != null) {
                                topMovieDescription.setText(randomMovie.getDescription());
                            }
                            String thumbnailUrl = Constants.BASE_URL + "/uploads/" + randomMovie.getThumbnailName();

                            // Set image using Glide
                            Glide.with(topMovieThumbnail.getContext())
                                    .load(Uri.parse(thumbnailUrl)) // URL of the image
                                    .placeholder(R.drawable.sample_thumbnail_background) // Default image while loading
                                    .error(R.drawable.sample_thumbnail_background) // If failed to load
                                    .into(topMovieThumbnail); // ImageView reference
                        }
                    }
                }
            });
        }
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(() -> {

                Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show();

                moviesListsViewModel.reload();
                categoriesViewModel.reload();

                new Handler().postDelayed(() -> {
                    // After loading is done, hide the spinner
                    swipeRefreshLayout.setRefreshing(false);
                }, 2000); // Simulating a 2-second delay
            });
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_categories) {
            showFloatingList();
        } else if (id == R.id.admin_screen) {
            Intent intent = new Intent(MainActivity.this, AdminActivity.class);

            startActivity(intent);
        } else if (id == R.id.nav_theme) {
            Toast.makeText(this, "Changed theme!", Toast.LENGTH_SHORT).show();
            ThemeUtils.toggleTheme(this);
        } else if (id == R.id.logout) {
            // Delete the shared preference of the JwtToken
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(MainActivity.this, StartingActivity.class);

            startActivity(intent);
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void showFloatingList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.floating_list, null);

        if (view == null) {
            return;
        }

        builder.setView(view);
        final AlertDialog dialog = builder.create();

        RecyclerView recyclerView = view.findViewById(R.id.categoriesRecyclerView);
        AppCompatImageButton closeButton = view.findViewById(R.id.closeButton);

        // Ensure RecyclerView exists
        if (recyclerView != null) {
            CategoriesViewModel categoriesViewModel = new ViewModelProvider(this, new CategoryViewModelFactory(this, jwtToken)).get(CategoriesViewModel.class);
            CategoryListAdapter adapter = new CategoryListAdapter(this, new ArrayList<>());

            categoriesViewModel.get().observe(this, categories -> {
                if (categories != null) {
                    adapter.setMovies(categories);
                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }

        // Ensure closeButton exists before setting listener
        if (closeButton != null) {
            closeButton.setOnClickListener(v -> dialog.dismiss());
        }

        dialog.show();
    }
}