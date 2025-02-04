package com.example.netflixadmin.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflixadmin.R;
import com.example.netflixadmin.data.local.CategoryEntity;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<CategoryEntity> categoryList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(CategoryEntity category);
        void onDeleteClick(CategoryEntity category);
    }

    public CategoryAdapter(List<CategoryEntity> categories, OnItemClickListener listener) {
        this.categoryList = categories;
        this.listener = listener;
    }

    public void setCategories(List<CategoryEntity> categories) {
        this.categoryList = categories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(categoryList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return categoryList == null ? 0 : categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final ImageView editButton;
        private final ImageView deleteButton;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.categoryName);
            editButton = itemView.findViewById(R.id.btnEditCategory);
            deleteButton = itemView.findViewById(R.id.btnDeleteCategory);
        }

        public void bind(final CategoryEntity category, final OnItemClickListener listener) {
            nameTextView.setText(category.getName());

            editButton.setOnClickListener(v -> listener.onEditClick(category));
            deleteButton.setOnClickListener(v -> listener.onDeleteClick(category));
        }
    }
}
