package com.example.fakeflix;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.fakeflix.databinding.ActivityLoginBinding;
import com.example.fakeflix.databinding.ActivityWatchMovieBinding;
import com.example.fakeflix.entities.Movie;
import com.squareup.picasso.Picasso;

public class WatchMovieActivity extends AppCompatActivity {

    private ImageView movieThumbnail;
    private TextView movieTitle;

    private String movieId;
    private Button watchMovieButton;
    private TextView movieDescription;

    private ActivityWatchMovieBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_movie);
        binding = ActivityWatchMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Glide.with(this);

        movieThumbnail = findViewById(R.id.movieThumbnail);
        movieTitle = findViewById(R.id.movieTitle);

        movieDescription = findViewById(R.id.movieDescription);

        Intent intent = getIntent();
        if (intent != null) {
            movieId = intent.getStringExtra("movieId");
            movieTitle.setText(intent.getStringExtra("movieTitle"));

            String imageUrl = intent.getStringExtra("movieThumbnail");
            Uri imageUri = Uri.parse(imageUrl);

//            String balls = "http://10.0.0.16:8080/uploads/1738672512999-951545829-match_distribution_20 (1).png";
//            Uri urlballs = Uri.parse(balls);

            Glide.with(this)
                    .load(imageUri)
                    .into(movieThumbnail);

            binding.movieDescription.setText("Description: " + intent.getStringExtra("movieDescription"));
            binding.movieLength.setText("Length: " + intent.getStringExtra("movieLength") + " minutes");

//            String[] categoriesArray = getIntent().getStringArrayExtra("movieCategories");
//
//            String categoriesText = TextUtils.join(", ", categoriesArray); // "Action, Drama, Comedy"
//            binding.movieCategories.setText("Categories: " + categoriesText);

        }

        watchMovieButton = findViewById(R.id.watchMovieButton);

        watchMovieButton.setOnClickListener(v -> {
            Intent intent2 = new Intent(WatchMovieActivity.this, VideoPlayerActivity.class);
            intent2.putExtra("movieVideo", intent.getStringExtra("movieVideo"));
            startActivity(intent2);
        });
    }
}
