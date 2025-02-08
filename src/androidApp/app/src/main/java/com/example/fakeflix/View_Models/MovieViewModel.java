package com.example.fakeflix.View_Models;

import androidx.lifecycle.ViewModel;
import com.example.fakeflix.Repositories.MovieRepository;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.MutableLiveData;

public class MovieViewModel extends ViewModel {
    private MovieRepository repository;
    private MutableLiveData<String> searchQuery = new MutableLiveData<>();

    public MovieViewModel(MovieRepository repository) {
        this.repository = repository;
    }

//    public LiveData<List<Movie>> getMovies() {
//        return repository.searchMovies(searchQuery.getValue() != null ? searchQuery.getValue() : "");
//    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final MovieRepository repository;

        public Factory(MovieRepository repository) {
            this.repository = repository;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new MovieViewModel(repository);
        }
    }
}

