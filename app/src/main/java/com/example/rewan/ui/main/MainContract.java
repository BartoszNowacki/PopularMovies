package com.example.rewan.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.example.rewan.base.MvpPresenter;
import com.example.rewan.base.MvpView;
import com.example.rewan.model.Movie;
import com.example.rewan.network.NetworkHelper;

import java.util.List;



public interface MainContract {

    interface View extends MvpView {
        void setMoviesAdapter(List<Movie> countries);
    }

    interface Presenter<V extends MvpView> extends MvpPresenter<V> {
        Intent configuredIntent(Intent intent, Movie movie);
        NetworkHelper getReceiver();
        IntentFilter getIntentFilter();
        void getMovies(Context context);
    }
}