package com.example.fakeflix.data.repository;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fakeflix.data.local.MovieDao;
import com.example.fakeflix.data.local.MovieDatabase;
import com.example.fakeflix.data.local.MovieEntity;
import com.example.fakeflix.data.remote.ApiService;
import com.example.fakeflix.data.remote.RetrofitClient;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private MovieDao dao;
    private ApiService apiService;
    private MovieListData movieListData;
    private String jwtToken;

    public MovieRepository(String jwtToken, Application application) {
        // Initialize local database and DAO
        MovieDatabase db = MovieDatabase.getInstance(application);
        dao = db.movieDao();

        // Initialize Retrofit API service
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Initialize MutableLiveData for movies
        movieListData = new MovieListData();
        this.jwtToken= jwtToken;
    }

    // Inner class for MutableLiveData
    class MovieListData extends MutableLiveData<List<MovieEntity>> {
        public MovieListData() {
            super();
            setValue(new LinkedList<>()); // Initialize with an empty list
        }

        @Override
        protected void onActive() {
            super.onActive();

            // Fetch data from the local database when the LiveData becomes active
            new Thread(() -> {
                List<MovieEntity> movies = dao.getAllMovies();
                postValue(movies); // Update LiveData with local data

                // Fetch data from the remote API and update the local database
                fetchMoviesFromApi();
            }).start();
        }

        private void fetchMoviesFromApi() {
            apiService.getAllMovies("Bearer" + jwtToken).enqueue(new Callback<List<MovieEntity>>() {
                @Override
                public void onResponse(Call<List<MovieEntity>> call, Response<List<MovieEntity>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Update LiveData with API data
                        postValue(response.body());

                        // Save API data to the local database
                        new Thread(() -> {
                            // Clear existing data in the local database
                            dao.deleteAllMovies();

                            // Insert new data from the API
                            for (MovieEntity movie : response.body()) {
                                dao.insertMovie(movie);
                            }
                        }).start();
                    }
                }

                @Override
                public void onFailure(Call<List<MovieEntity>> call, Throwable t) {
                    // Handle API call failure
                    t.printStackTrace();
                }
            });
        }
    }

    // Method to get all movies as LiveData
    public LiveData<List<MovieEntity>> getAllMovies() {
        return movieListData;
    }

    // Method to add a new movie
    public void addMovie(MovieEntity movie, Callback<MovieEntity> callback) {

        apiService.addMovie("Bearer" + jwtToken, movie).enqueue(callback);
    }

    // Method to update a movie
    public void updateMovie(MovieEntity movie, Callback<MovieEntity> callback) {
        apiService.updateMovie("Bearer" + jwtToken,movie.getId(), movie).enqueue(callback);
    }

    // Method to delete a movie
    public void deleteMovie(String id, Callback<Void> callback) {
        apiService.deleteMovie("Bearer" + jwtToken, id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Delete movie from local database after successful API deletion
                    new Thread(() -> {
                        dao.deleteMovie(id);
                    }).start();
                    callback.onResponse(call, response); // Notify success
                } else {
                    Log.d("Failed Deletion", response.message() + response.code());
                    callback.onFailure(call, new Throwable("Failed to delete movie from server"));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(call, t); // Notify failure
            }
        });
    }


    // Method to search for a movie by ID
// Method to search for a movie by ID
    public LiveData<MovieEntity> getMovieById(String id) {
        MutableLiveData<MovieEntity> movieLiveData = new MutableLiveData<>();

        // Fetch movie from local database
        new Thread(() -> {
            MovieEntity movie = dao.getMovieById(id);
            if (movie != null) {
                movieLiveData.postValue(movie);
            }
        }).start();

        // Fetch movie from remote API
        apiService.getMovieById("Bearer" + jwtToken, id).enqueue(new Callback<MovieEntity>() {
            @Override
            public void onResponse(Call<MovieEntity> call, Response<MovieEntity> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Perform database insertion on a background thread
                    new Thread(() -> {
                        dao.insertMovie(response.body()); // Ensure this is a method in your DAO for inserting a movie
                    }).start();

                    movieLiveData.postValue(response.body());
                } else {
                    movieLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<MovieEntity> call, Throwable t) {
                movieLiveData.postValue(null);
            }
        });

        return movieLiveData;
    }



    public LiveData<Pair<String, String>> uploadImage(Uri imageUri, Context context) {
        MutableLiveData<Pair<String, String>> resultLiveData = new MutableLiveData<>();
        MultipartBody.Part imagePart = prepareFilePartImage(imageUri, context);

        apiService.uploadImage("Bearer" + jwtToken, imagePart).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        String imageUrl = jsonResponse.getString("url"); // Assuming API returns 'url'
                        String imageName = jsonResponse.getString("name");
                        resultLiveData.postValue(new Pair<>(imageName, imageUrl)); // Post Pair of name and URL
                    } catch (Exception e) {
                        e.printStackTrace();
                        resultLiveData.postValue(null);
                    }
                } else {
                    resultLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                resultLiveData.postValue(null);
            }
        });

        return resultLiveData;
    }


    public LiveData<Pair<String, String>> uploadVideo(Uri videoUri, Context context) {
        MutableLiveData<Pair<String, String>> resultLiveData = new MutableLiveData<>();
        MultipartBody.Part videoPart = prepareFilePartVideo(videoUri, context);

        apiService.uploadVideo("Bearer" + jwtToken,videoPart).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        String videoUrl = jsonResponse.getString("url");
                        String videoName = jsonResponse.getString("name");
                        resultLiveData.postValue(new Pair<>(videoName, videoUrl)); // Post Pair of name and URL
                    } catch (Exception e) {
                        Log.e("Upload Video", "Exception parsing response", e);
                        resultLiveData.postValue(null);
                    }
                } else {
                    Log.d("NOT GOOD", "OOPS FELL OF 1: " + response.code() + " - " + response.message());
                    resultLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("NOT GOOD", "OOPS FELL OF 2: " + t.getMessage());
                resultLiveData.postValue(null);
            }
        });

        return resultLiveData;
    }



    private MultipartBody.Part prepareFilePartVideo(Uri uri, Context context) {
        File file = getFileFromUriVideo(uri, context);
        String mimeType = context.getContentResolver().getType(uri);
        if (mimeType == null) mimeType = "video/mp4";

        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
        return MultipartBody.Part.createFormData("files", file.getName(), requestFile);
    }

    private MultipartBody.Part prepareFilePartImage(Uri uri, Context context) {
        File file = getFileFromUriImage(uri, context);
        String mimeType = context.getContentResolver().getType(uri);
        if (mimeType == null) mimeType = "image/jpg";

        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
        return MultipartBody.Part.createFormData("files", file.getName(), requestFile);
    }


    private File getFileFromUriVideo(Uri uri, Context context) {
        File file = new File(context.getCacheDir(), "upload_temp.mp4");
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             FileOutputStream outputStream = new FileOutputStream(file)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
    private File getFileFromUriImage(Uri uri, Context context) {
        File file = new File(context.getCacheDir(), "upload_temp.jpg");
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             FileOutputStream outputStream = new FileOutputStream(file)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}