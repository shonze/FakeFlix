package com.example.advanced_programing_ex_4.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.advanced_programing_ex_4.R;
import com.example.advanced_programing_ex_4.entities.MoviesList;

import java.util.ArrayList;
import java.util.List;

public class MoviesListsAdapter extends RecyclerView.Adapter<MoviesListsAdapter.MovieListViewHolder> {

    private List<MoviesList> moviesLists;
    private final LayoutInflater mInflater;

    public MoviesListsAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        moviesLists = new ArrayList<>();
    }

    public List<MoviesList> getMoviesLists() {
        return moviesLists;
    }

    @NonNull
    @Override
    public MovieListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.movie_list_layout, parent, false);
        return new MovieListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListViewHolder holder, int position) {
        MoviesList currentMoviesList = moviesLists.get(position);
        holder.listTitleTextView.setText(currentMoviesList.getCategoryName());

        if (holder.moviesRecyclerView.getAdapter() == null) {
            MovieAdapter moviesAdapter = new MovieAdapter(holder.itemView.getContext(), currentMoviesList.getMoviesList());
            holder.moviesRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            holder.moviesRecyclerView.setAdapter(moviesAdapter);
        } else {
            ((MovieAdapter) holder.moviesRecyclerView.getAdapter()).setMovies(currentMoviesList.getMoviesList());
        }
    }

    @Override
    public int getItemCount() {
        return (moviesLists != null) ? moviesLists.size() : 0;
    }

    public void setMoviesLists(List<MoviesList> MoviesList) {
        this.moviesLists = MoviesList;
        notifyDataSetChanged();
    }

    static class MovieListViewHolder extends RecyclerView.ViewHolder {
        private final TextView listTitleTextView;
        private final RecyclerView moviesRecyclerView;

        public MovieListViewHolder(@NonNull View itemView) {
            super(itemView);
            listTitleTextView = itemView.findViewById(R.id.categoryTitle);
            moviesRecyclerView = itemView.findViewById(R.id.moviesRecyclerView);
        }
    }
}
