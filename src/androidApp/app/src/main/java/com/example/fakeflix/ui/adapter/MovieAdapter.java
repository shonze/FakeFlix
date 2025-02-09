package com.example.fakeflix.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fakeflix.R;
import com.example.fakeflix.data.local.MovieEntity;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<MovieEntity> movieList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(MovieEntity movie);
        void onDeleteClick(MovieEntity movie);
    }

    public MovieAdapter(List<MovieEntity> movies, OnItemClickListener listener) {
        this.movieList = movies;
        this.listener = listener;
    }

    public void setMovies(List<MovieEntity> movies) {
        this.movieList = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(movieList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView descriptionTextView;
        private final TextView lengthTextView;
        private final ImageView thumbnailImageView;
        private final ImageView editButton;
        private final ImageView deleteButton;

        public MovieViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.movieTitle);
            descriptionTextView = itemView.findViewById(R.id.movieDescription);
            lengthTextView = itemView.findViewById(R.id.movieLength);
            thumbnailImageView = itemView.findViewById(R.id.movieThumbnail);
            editButton = itemView.findViewById(R.id.btnEdit);
            deleteButton = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(final MovieEntity movie, final OnItemClickListener listener) {
            titleTextView.setText(movie.title);
            descriptionTextView.setText(movie.description);
            lengthTextView.setText("Length: " + movie.length + " min");

            // Load the movie thumbnail using Glide
            Glide.with(itemView.getContext())
                    .load(movie.thumbnail)
                    .into(thumbnailImageView);

            editButton.setOnClickListener(v -> listener.onEditClick(movie));
            deleteButton.setOnClickListener(v -> listener.onDeleteClick(movie));
        }
    }
}
