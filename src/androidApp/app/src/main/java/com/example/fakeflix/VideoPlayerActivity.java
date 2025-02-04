package com.example.fakeflix;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;
//
//public class VideoPlayerActivity extends AppCompatActivity {
//
//    private PlayerView playerView;
//    private ExoPlayer player;
//    private String videoUrl;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_video_player);
//
//        playerView = findViewById(R.id.playerView);
//        ImageButton backButton = findViewById(R.id.backButton);
//
//        // Get video URL from intent
//        Intent intent = getIntent();
//        if (intent != null) {
//            videoUrl = intent.getStringExtra("videoUrl");
//        }
//
//        // Initialize ExoPlayer
//        initializePlayer();
//
//        // Handle back button
//        backButton.setOnClickListener(v -> onBackPressed());
//    }
//
//    private void initializePlayer() {
//        if (videoUrl == null || videoUrl.isEmpty()) {
//            finish();
//            return;
//        }
//
//        player = new ExoPlayer.Builder(this).build();
//        playerView.setPlayer(player);
//
//        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoUrl));
//        player.setMediaItem(mediaItem);
//        player.prepare();
//        player.play();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (player != null) {
//            player.pause();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (player != null) {
//            player.release();
//            player = null;
//        }
//    }
//}
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fakeflix.R;

//public class VideoPlayerActivity extends AppCompatActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_video_player);
//
//        VideoView videoView = findViewById(R.id.videoView);
//        String videoUrl = getIntent().getStringExtra("videoUrl");
//
//        if (videoUrl != null) {
//            Uri uri = Uri.parse(videoUrl);
//            videoView.setVideoURI(uri);
//
//            // Add media controls (play, pause, seek)
//            MediaController mediaController = new MediaController(this);
//            mediaController.setAnchorView(videoView);
//            videoView.setMediaController(mediaController);
//
//            videoView.start(); // Start playing the video
//        }
//    }
//}



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;

public class VideoPlayerActivity extends AppCompatActivity {

    private PlayerView playerView;
    private ExoPlayer player;
    private String videoUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        playerView = findViewById(R.id.playerView);
        ImageButton backButton = findViewById(R.id.backButton);

        // Get video URL from intent
        Intent intent = getIntent();
        if (intent != null) {
            videoUrl = intent.getStringExtra("movieVideo");

        }

        // Initialize ExoPlayer
        initializePlayer();

        // Handle back button
        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void initializePlayer() {
        if (videoUrl == null || videoUrl.isEmpty()) {
            finish();
            return;
        }

        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoUrl));
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}


