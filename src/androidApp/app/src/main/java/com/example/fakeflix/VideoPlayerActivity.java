package com.example.fakeflix;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;

import android.os.Handler;
import android.widget.SeekBar;

//
//public class VideoPlayerActivity extends AppCompatActivity {
//
//    private PlayerView playerView;
//    private ExoPlayer player;
//    private String videoUrl;
//    private ImageButton backButton;
//    private ImageButton playPauseButton;
//    private ImageButton skipForwardButton;
//    private ImageButton skipBackwardButton;
//    private Handler hideHandler = new Handler();
//    private Runnable hideControlsRunnable;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_video_player);
//
//        playerView = findViewById(R.id.playerView);
//        backButton = findViewById(R.id.backButton);
//        playPauseButton = findViewById(R.id.playPauseButton);
//        skipForwardButton = findViewById(R.id.skipForwardButton);
//        skipBackwardButton = findViewById(R.id.skipBackwardButton);
//
//        // Initialize the runnable for hiding the controls
//        hideControlsRunnable = () -> {
//            backButton.setVisibility(View.GONE);
//            playPauseButton.setVisibility(View.GONE);
//            skipForwardButton.setVisibility(View.GONE);
//            skipBackwardButton.setVisibility(View.GONE);
//        };
//
//        // Hide controls after 3 seconds initially
//        hideHandler.postDelayed(hideControlsRunnable, 3000);
//
//        // Show controls when screen is touched
//        playerView.setOnTouchListener((v, event) -> {
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                showControls();
//            }
//            return false;
//        });
//
//        // Handle back button click
//        backButton.setOnClickListener(v -> onBackPressed());
//
//        // Handle play/pause button click
//        playPauseButton.setOnClickListener(v -> togglePlayPause());
//
//        // Handle skip forward button click
//        skipForwardButton.setOnClickListener(v -> skipForward());
//
//        // Handle skip backward button click
//        skipBackwardButton.setOnClickListener(v -> skipBackward());
//
//        // Get video URL from intent
//        Intent intent = getIntent();
//        if (intent != null) {
//            videoUrl = intent.getStringExtra("movieVideo");
//        }
//
//        initializePlayer();
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
//
//        // Update play/pause button icon based on player state
//        player.addListener(new Player.Listener() {
//            @Override
//            public void onPlaybackStateChanged(int playbackState) {
//                if (playbackState == Player.STATE_READY) {
//                    playPauseButton.setImageResource(
//                            player.isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play
//                    );
//                }
//            }
//        });
//    }
//
//    private void showControls() {
//        backButton.setVisibility(View.VISIBLE);
//        playPauseButton.setVisibility(View.VISIBLE);
//        skipForwardButton.setVisibility(View.VISIBLE);
//        skipBackwardButton.setVisibility(View.VISIBLE);
//
//        // Hide controls again after 3 seconds of inactivity
//        hideHandler.removeCallbacks(hideControlsRunnable); // Clear any pending hides
//        hideHandler.postDelayed(hideControlsRunnable, 3000);
//    }
//
//    private void togglePlayPause() {
//        if (player.isPlaying()) {
//            player.pause();
//            playPauseButton.setImageResource(R.drawable.ic_play);
//        } else {
//            player.play();
//            playPauseButton.setImageResource(R.drawable.ic_pause);
//        }
//    }
//
//    private void skipForward() {
//        if (player != null) {
//            long currentPosition = player.getCurrentPosition();
//            player.seekTo(currentPosition + 5000); // Skip forward by 5 seconds (5000 ms)
//        }
//    }
//
//    private void skipBackward() {
//        if (player != null) {
//            long currentPosition = player.getCurrentPosition();
//            player.seekTo(currentPosition - 5000); // Skip backward by 5 seconds (5000 ms)
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
//        hideHandler.removeCallbacksAndMessages(null);
//    }
//}

public class VideoPlayerActivity extends AppCompatActivity {

    private PlayerView playerView;
    private ExoPlayer player;
    private String videoUrl;
    private ImageButton backButton;
    private ImageButton playPauseButton;
    private ImageButton skipForwardButton;
    private ImageButton skipBackwardButton;
    private SeekBar seekBar;
    private Handler hideHandler = new Handler();
    private Runnable hideControlsRunnable;
    private boolean isSeeking = false; // To track if the user is manually seeking

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        playerView = findViewById(R.id.playerView);
        backButton = findViewById(R.id.backButton);
        playPauseButton = findViewById(R.id.playPauseButton);
        skipForwardButton = findViewById(R.id.skipForwardButton);
        skipBackwardButton = findViewById(R.id.skipBackwardButton);
        seekBar = findViewById(R.id.seekBar);

        // Initialize the runnable for hiding the controls
        hideControlsRunnable = () -> {
            backButton.setVisibility(View.GONE);
            playPauseButton.setVisibility(View.GONE);
            skipForwardButton.setVisibility(View.GONE);
            skipBackwardButton.setVisibility(View.GONE);
            seekBar.setVisibility(View.GONE);
        };

        // Hide controls after 3 seconds initially
        hideHandler.postDelayed(hideControlsRunnable, 3000);

        // Show controls when screen is touched
        playerView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                showControls();
            }
            return false;
        });

        // Handle back button click
        backButton.setOnClickListener(v -> onBackPressed());

        // Handle play/pause button click
        playPauseButton.setOnClickListener(v -> togglePlayPause());

        // Handle skip forward button click
        skipForwardButton.setOnClickListener(v -> skipForward());

        // Handle skip backward button click
        skipBackwardButton.setOnClickListener(v -> skipBackward());

        // Handle seek bar changes
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    player.seekTo(progress * 1000L); // Convert seconds to milliseconds
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeeking = false;
            }
        });

        // Get video URL from intent
        Intent intent = getIntent();
        if (intent != null) {
            videoUrl = intent.getStringExtra("movieVideo");
        }

        initializePlayer();
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

        // Update play/pause button icon based on player state
        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_READY) {
                    playPauseButton.setImageResource(
                            player.isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play
                    );
                }
            }
        });

        // Update seek bar progress
        player.addListener(new Player.Listener() {
            @Override
            public void onPositionDiscontinuity(@NonNull Player.PositionInfo oldPosition, @NonNull Player.PositionInfo newPosition, int reason) {
                if (!isSeeking) {
                    updateSeekBar();
                }
            }
        });

        // Start a periodic task to update the seek bar
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isSeeking && player != null && player.isPlaying()) {
                    updateSeekBar();
                }
                hideHandler.postDelayed(this, 1000); // Update every second
            }
        }, 1000);
    }

    private void showControls() {
        backButton.setVisibility(View.VISIBLE);
        playPauseButton.setVisibility(View.VISIBLE);
        skipForwardButton.setVisibility(View.VISIBLE);
        skipBackwardButton.setVisibility(View.VISIBLE);
        seekBar.setVisibility(View.VISIBLE);

        // Hide controls again after 3 seconds of inactivity
        hideHandler.removeCallbacks(hideControlsRunnable); // Clear any pending hides
        hideHandler.postDelayed(hideControlsRunnable, 3000);
    }

    private void togglePlayPause() {
        if (player.isPlaying()) {
            player.pause();
            playPauseButton.setImageResource(R.drawable.ic_play);
        } else {
            player.play();
            playPauseButton.setImageResource(R.drawable.ic_pause);
        }
    }

    private void skipForward() {
        if (player != null) {
            long currentPosition = player.getCurrentPosition();
            player.seekTo(currentPosition + 5000); // Skip forward by 5 seconds (5000 ms)
        }
    }

    private void skipBackward() {
        if (player != null) {
            long currentPosition = player.getCurrentPosition();
            player.seekTo(currentPosition - 5000); // Skip backward by 5 seconds (5000 ms)
        }
    }

    private void updateSeekBar() {
        if (player != null) {
            long duration = player.getDuration(); // Total duration in milliseconds
            long position = player.getCurrentPosition(); // Current position in milliseconds
            seekBar.setMax((int) (duration / 1000)); // Convert to seconds
            seekBar.setProgress((int) (position / 1000)); // Convert to seconds
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
        hideHandler.removeCallbacksAndMessages(null);
    }
}