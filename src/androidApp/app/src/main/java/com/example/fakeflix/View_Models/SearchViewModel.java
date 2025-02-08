package com.example.fakeflix.View_Models;//package com.example.fakeflix.View_Models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.fakeflix.Repositories.MovieRepository;
import com.example.fakeflix.entities.Movie;
import java.util.List;

public class SearchViewModel extends ViewModel {
    private final MovieRepository repository;
    private final MutableLiveData<List<Movie>> movies = new MutableLiveData<>();
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");

    public SearchViewModel(MovieRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Movie>> getMovies(String token) {
        return repository.searchMovies(token, searchQuery.getValue() != null ? searchQuery.getValue() : "");
    }

    public void setSearchQuery(String query, String token) {
        searchQuery.setValue(query);
        searchMovies(token, query);
    }

    public void searchMovies(String token, String query) {
        LiveData<List<Movie>> movieLiveData = repository.searchMovies(token, query);
        movieLiveData.observeForever(movies::postValue);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final MovieRepository repository;

        public Factory(MovieRepository repository) {
            this.repository = repository;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(SearchViewModel.class)) {
                return (T) new SearchViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
