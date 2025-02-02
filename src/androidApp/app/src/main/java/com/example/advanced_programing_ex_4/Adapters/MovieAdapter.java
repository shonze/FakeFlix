package com.example.advanced_programing_ex_4.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.advanced_programing_ex_4.R;
import com.example.advanced_programing_ex_4.entities.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<String> moviesIds;
    private final LayoutInflater inflater;

    public MovieAdapter(Context context, List<String> movies) {
        this.inflater = LayoutInflater.from(context);
        this.moviesIds = (movies != null) ? movies : new ArrayList<>();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.movie_layout, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (position < moviesIds.size()) {
            Movie currentMovie = movies.get(position);
            holder.movieTitleTextView.setText(currentMovie.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return moviesIds.size();
    }

    public List<String> getMovies() {
        return moviesIds;
    }

    public void setMovies(List<String> moviesList) {
        this.moviesIds = (moviesList != null) ? moviesList : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private final TextView movieTitleTextView;

        public MovieViewHolder(@NonNull View itemView) {
            // The movie should appear as an image that can be clicked.
            super(itemView);
            movieTitleTextView = itemView.findViewById(R.id.movieTitle);
        }
    }
}
