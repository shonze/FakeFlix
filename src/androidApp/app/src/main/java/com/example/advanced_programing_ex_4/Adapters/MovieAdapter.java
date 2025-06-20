package com.example.advanced_programing_ex_4.Adapters;

import static com.example.advanced_programing_ex_4.MyApplication.context;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fakeflix.R;
import com.example.advanced_programing_ex_4.entities.Movie;
import com.example.fakeflix.WatchMovieActivity;
import com.example.fakeflix.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> moviesList;
    private final LayoutInflater inflater;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.inflater = LayoutInflater.from(context);
        this.moviesList = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.movie_layout, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (position < moviesList.size()) {
            Movie currentMovie = moviesList.get(position);

            holder.movieTitleTextView.setText(currentMovie.getTitle());

            String thumbnailUrl = Constants.BASE_URL + "/uploads/" + currentMovie.getThumbnailName();

            // Set image using Glide
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(thumbnailUrl)) // URL of the image
                    .placeholder(R.drawable.sample_thumbnail_background) // Default image while loading
                    .error(R.drawable.sample_thumbnail_background) // If failed to load
                    .into(holder.movieThumbnail); // ImageView reference

            holder.movieThumbnail.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), WatchMovieActivity.class);

                intent.putExtra("movieId", currentMovie.getMovieId());
                intent.putExtra("movieTitle", currentMovie.getTitle());
                intent.putExtra("movieThumbnail", Constants.BASE_URL + "/uploads/" + currentMovie.getThumbnailName());
                intent.putExtra("movieVideo", Constants.BASE_URL + "/uploads/" + currentMovie.getVideoName());
                intent.putExtra("movieDescription",  currentMovie.getDescription());
                intent.putExtra("movieLength", currentMovie.getLength());
                intent.putExtra("movieCategories", currentMovie.getCategories().toArray(new String[0]));

                v.getContext().startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public List<Movie> getMovies() {
        return moviesList;
    }

    public void setMovies(List<Movie> moviesList) {
        this.moviesList = (moviesList != null) ? moviesList : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private final TextView movieTitleTextView;
        private final ImageView movieThumbnail;
        public MovieViewHolder(@NonNull View itemView) {
            // The movie should appear as an image that can be clicked.
            super(itemView);
            this.movieTitleTextView = itemView.findViewById(R.id.movie_title);
            this.movieThumbnail = itemView.findViewById(R.id.movie_image);
        }
    }
}
