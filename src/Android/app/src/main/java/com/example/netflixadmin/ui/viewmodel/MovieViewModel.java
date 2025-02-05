package com.example.netflixadmin.ui.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

import androidx.core.util.Pair;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.netflixadmin.data.local.MovieEntity;
import com.example.netflixadmin.data.repository.MovieRepository;

import com.example.netflixadmin.data.local.CategoryEntity;
import com.example.netflixadmin.data.repository.CategoryRepository;

import java.util.List;

import retrofit2.Callback;

public class MovieViewModel extends AndroidViewModel {
    private final MovieRepository repository;
    private final CategoryRepository categoryRepository;
    public MutableLiveData<List<MovieEntity>> moviesLiveData = new MutableLiveData<>();
    private MutableLiveData<Pair<String,String>> thumbnailUrl = new MutableLiveData<>();
    private MutableLiveData<Pair<String,String>> videoUrl = new MutableLiveData<>();


    public LiveData<Pair<String,String>> getThumbnailUrl() {
        return thumbnailUrl;
    }

    public LiveData<Pair<String,String>> getVideoUrl() {
        return videoUrl;
    }

    public void uploadThumbnail(Uri imageUri, Context context) {
        repository.uploadImage(imageUri, context).observeForever(pair -> {
            if (pair != null) {
                String imageName = pair.first; // Get the image name
                String imageUrl = pair.second; // Get the image URL
                if (imageUrl != null && imageName != null) {
                    thumbnailUrl.setValue(pair); // Set the value to thumbnailUrl
                }
            }
        });
    }



    public void uploadVideo(Uri videoUri, Context context) {
        repository.uploadVideo(videoUri, context).observeForever(pair -> {
            if (pair != null) {
                String videoName = pair.first; // Get the video name
                String video_url = pair.second; // Get the video URL
                if (video_url != null && videoName != null) {
                    videoUrl.setValue(pair); // Set the value to videoUrl
                }
            }
        });
    }


    public MovieViewModel(Application application) {
        super(application);
        repository = new MovieRepository(application);
        categoryRepository = new CategoryRepository(application);
    }

    public  LiveData<List<CategoryEntity>> getCategories(){
        return categoryRepository.getAllCategories();
    }

    public void fetchMovies() {
        repository.getAllMovies().observeForever(moviesLiveData::setValue);
    }

    public void addMovie(MovieEntity movie, Callback<MovieEntity> callback) {
        repository.addMovie(movie, callback);
    }

    public void updateMovie(MovieEntity movie, Callback<MovieEntity> callback) {
        repository.updateMovie(movie, callback);
    }

    public void deleteMovie(String id, Callback<Void> callback) {
        repository.deleteMovie(id, callback);
    }

    public LiveData<MovieEntity> getMovieById(String id) {
        return repository.getMovieById(id);
    }



}