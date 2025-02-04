package com.example.fakeflix;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.fakeflix.entities.Movie;

//public class DescriptionActivity extends AppCompatActivity {
//
//    private ImageView movieThumbnail;
//    private TextView movieTitle, movieDescription;
//    private Button watchMovieButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_description);
//
//        movieThumbnail = findViewById(R.id.movieThumbnail);
//        movieTitle = findViewById(R.id.movieTitle);
//        movieDescription = findViewById(R.id.movieDescription);
//        watchMovieButton = findViewById(R.id.watchMovieButton);
//
//        // Retrieve movie object from intent
//        Movie movie = (Movie) getIntent().getSerializableExtra("movie");
//
//        if (movie != null) {
//            Glide.with(this).load(movie.getThumbnail()).into(movieThumbnail);
//            movieTitle.setText(movie.getTitle());
//            movieDescription.setText(movie.getDescription());
//        }
//
//        watchMovieButton.setOnClickListener(v -> {
//            Intent intent = new Intent(this, VideoPlayerActivity.class);
//            intent.putExtra("movieUrl", movie.getVideo());
//            startActivity(intent);
//        });
//    }
//}

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.fakeflix.R;
import com.example.fakeflix.entities.Movie;

//public class DescriptionActivity extends AppCompatActivity {
//    private Movie movie;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_description);
//
//        TextView titleTextView = findViewById(R.id.movieTitle);
//        TextView descriptionTextView = findViewById(R.id.movieDescription);
//        TextView categoriesTextView = findViewById(R.id.movieCategories);
//        TextView lengthTextView = findViewById(R.id.movieLength);
//        ImageView thumbnailImageView = findViewById(R.id.movieThumbnail);
//        Button playButton = findViewById(R.id.playButton);
//
//        // Get the movie object from Intent
//        movie = (Movie) getIntent().getSerializableExtra("movie");
//
//        if (movie != null) {
//            titleTextView.setText(movie.getTitle());
//            descriptionTextView.setText(movie.getDescription());
//            lengthTextView.setText("Duration: " + movie.getLength());
//            categoriesTextView.setText("Categories: " + String.join(", ", movie.getCategories()));
//
//            // Load movie thumbnail using Glide
//            Glide.with(this)
//                    .load(movie.getThumbnail())
//                    .placeholder(R.drawable.search_background)
//                    .into(thumbnailImageView);
//
//            // Play button click - Navigate to VideoPlayerActivity
//            playButton.setOnClickListener(v -> {
//                Intent intent = new Intent(DescriptionActivity.this, VideoPlayerActivity.class);
//                intent.putExtra("videoUrl", movie.getVideo()); // Pass video URL
//                startActivity(intent);
//            });
//        }
//    }
//}

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.fakeflix.entities.Movie;

public class DescriptionActivity extends AppCompatActivity {

    private ImageView movieThumbnail;
    private TextView movieTitle, movieDescription;
    private Button watchMovieButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        movieThumbnail = findViewById(R.id.movieThumbnail);
        movieTitle = findViewById(R.id.movieTitle);
        movieDescription = findViewById(R.id.movieDescription);
        watchMovieButton = findViewById(R.id.watchMovieButton);

        // Retrieve movie object from intent
        Movie movie = (Movie) getIntent().getSerializableExtra("movie");

        if (movie != null) {
            Glide.with(this).load(movie.getThumbnail()).into(movieThumbnail);
            movieTitle.setText(movie.getTitle());
            movieDescription.setText(movie.getDescription());
        }

        watchMovieButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, VideoPlayerActivity.class);
            intent.putExtra("movieUrl", movie.getVideo());
            startActivity(intent);
        });
    }
}
