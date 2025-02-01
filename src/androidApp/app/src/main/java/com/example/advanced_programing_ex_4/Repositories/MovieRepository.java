//package com.example.advanced_programing_ex_4.Repository;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//
//import com.example.advanced_programing_ex_4.entities.Movie;
//
//import java.util.LinkedList;
//import java.util.List;
//
//public class MovieRepository {
//
//     private MovieDao dao;
//     private MovieListData postListData;
//
//     public MovieRepository() {
//         LocalDatabase db = LocalDatabase.getInstance();
//         dao = db.moviesDao();
//         postListData = new PostListData();
//     }
//
//     class PostListData extends MutableLiveData<List<Movie>> {
//
//         public PostListData() {
//            super();
//            setValue(new LinkedList<Movie>());
//         }
//
//         @Override
//         protected void onActive() {
//            super.onActive();
//
//            new Thread(() -> {
//              postListData.postValue(dao.get());
//              }).start();
//            }
//    }
//
//    public LiveData<List<Movie>> getAll() {
//        return postListData;
//    }
//}