package com.example.advanced_programing_ex_4.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.advanced_programing_ex_4.R;
import com.example.advanced_programing_ex_4.CategoryMoviesActivity;
import com.example.advanced_programing_ex_4.entities.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryListViewHolder>{
    private List<Category> categoryList;
    private final LayoutInflater inflater;
    private Context context;

    public CategoryListAdapter(Context context, List<Category> categoryList) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_list, parent, false);
        return new CategoryListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListViewHolder holder, int position) {
        if (position < categoryList.size()) {
            Category currentCategory = categoryList.get(position);

            holder.categoryButton.setText(currentCategory.getName());
            holder.categoryButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, CategoryMoviesActivity.class);
                intent.putExtra("category",currentCategory);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public List<Category> getMovies() {
        return categoryList;
    }

    public void setMovies(List<Category> categoryList) {
        this.categoryList = (categoryList != null) ? categoryList : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class CategoryListViewHolder extends RecyclerView.ViewHolder {
        private final Button categoryButton;

        public CategoryListViewHolder(@NonNull View itemView) {
            // The movie should appear as an image that can be clicked.
            super(itemView);
            categoryButton = itemView.findViewById(R.id.CategoryName);
        }
    }
}
