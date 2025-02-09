package com.example.fakeflix.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fakeflix.data.local.CategoryDao;
import com.example.fakeflix.data.local.CategoryDatabase;
import com.example.fakeflix.data.local.CategoryEntity;
import com.example.fakeflix.data.remote.ApiService;
import com.example.fakeflix.data.remote.RetrofitClient;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {
    private CategoryDao dao;
    private ApiService apiService;
    private CategoryListData categoryListData;
    private String jwtToken;

    public CategoryRepository(String jwtToken, Application application) {
        // Initialize local database and DAO
        CategoryDatabase db = CategoryDatabase.getInstance(application);
        dao = db.categoryDao();

        // Initialize Retrofit API service
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Initialize MutableLiveData for categories
        categoryListData = new CategoryListData();
        this.jwtToken= jwtToken;
    }

    // Inner class for MutableLiveData
    class CategoryListData extends MutableLiveData<List<CategoryEntity>> {
        public CategoryListData() {
            super();
            setValue(new LinkedList<>()); // Initialize with an empty list
        }

        @Override
        protected void onActive() {
            super.onActive();

            // Fetch data from the local database when the LiveData becomes active
            new Thread(() -> {
                List<CategoryEntity> categories = dao.getAllCategories();
                postValue(categories); // Update LiveData with local data

                // Fetch data from the remote API and update the local database
                fetchCategoriesFromApi();
            }).start();
        }

        private void fetchCategoriesFromApi() {
            apiService.getAllCategories("Bearer " + jwtToken).enqueue(new Callback<List<CategoryEntity>>() {
                @Override
                public void onResponse(Call<List<CategoryEntity>> call, Response<List<CategoryEntity>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Update LiveData with API data
                        postValue(response.body());

                        // Save API data to the local database
                        new Thread(() -> {
                            // Clear existing data in the local database
                            dao.deleteAllCategories();

                            // Insert new data from the API
                            for (CategoryEntity category : response.body()) {
                                dao.insertCategory(category);
                            }
                        }).start();
                    }
                }

                @Override
                public void onFailure(Call<List<CategoryEntity>> call, Throwable t) {
                    // Handle API call failure
                    t.printStackTrace();
                }
            });
        }
    }

    // Method to get all categories as LiveData
    public LiveData<List<CategoryEntity>> getAllCategories() {
        categoryListData.fetchCategoriesFromApi();
        return categoryListData;
    }

    // Method to add a new category
    public void addCategory(CategoryEntity category, Callback<CategoryEntity> callback) {
        apiService.addCategory("Bearer " + jwtToken, category).enqueue(callback);
    }

    // Method to update a category
    public void updateCategory(String id, CategoryEntity category, Callback<CategoryEntity> callback) {
        apiService.updateCategory("Bearer " + jwtToken, id, category).enqueue(callback);
    }

    // Method to delete a category
    public void deleteCategory(String id, Callback<Void> callback) {
        apiService.deleteCategory("Bearer " + jwtToken, id).enqueue(callback);
    }
}