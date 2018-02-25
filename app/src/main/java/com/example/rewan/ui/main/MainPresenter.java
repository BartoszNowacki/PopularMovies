package com.example.rewan.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

import com.example.rewan.R;
import com.example.rewan.base.BasePresenter;
import com.example.rewan.model.Movie;
import com.example.rewan.network.NetworkHelper;
import com.example.rewan.network.NetworkStateDataListener;
import com.example.rewan.retrofit.DataService;
import com.example.rewan.ui.singlemovie.SingleMovieActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainPresenter
        extends BasePresenter<MainContract.View>
        implements MainContract.Presenter<MainContract.View>, NetworkStateDataListener {

    private final String API_KEY;
    private NetworkHelper networkHelper;
    private DataService dataService;
    private boolean isTopCategory = false;

    MainPresenter(DataService dataService, String API_KEY) {
        networkHelper = new NetworkHelper(this);
        this.dataService = dataService;
        this.API_KEY = API_KEY;
    }

    @Override
    public void getMovies(Context context) {
        if (networkHelper.isNetworkAvailable(context)) {
            makeMovieCall();
        } else {
            view.showMessage(R.string.network_disabled);
        }
    }

    @Override
    public void makeMovieCall() {
        Call<JsonObject> moviesCall = getSortOrder();
        moviesCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    List<Movie> moviesList = convertResponse(response.body());
                    if (isViewAttached()) {
                        view.setMoviesAdapter(moviesList);
                    }
                } else {
                    int httpCode = response.code();
                    view.showErrorMessage(String.valueOf(httpCode));
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                view.showErrorMessage(t.getMessage());
            }
        });
    }

    public Call<JsonObject> getSortOrder() {
        if (isTopCategory) {
            return dataService.loadPopularMovies(API_KEY);
        } else {
            return dataService.loadTopMovies(API_KEY);
        }
    }
    /**
     * Sets variable isTopCategory
     * @param isTopCategory boolean to set category
     */
    public void setTopCategory(boolean isTopCategory) {
        this.isTopCategory = isTopCategory;
    }

    private List<Movie> convertResponse(JsonObject restResponse) {
        JsonArray moviesJsonArray = restResponse.getAsJsonArray("results");
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Movie>>() {
        }.getType();

        return (List<Movie>) gson.fromJson(moviesJsonArray, listType);
    }

    public IntentFilter getIntentFilter() {
        return networkHelper.getIntentFilter();
    }

    public NetworkHelper getReceiver() {
        return networkHelper;
    }

    public Intent configuredIntent(Intent intent, Movie movie) {
        intent.putExtra(SingleMovieActivity.MOVIE_TITLE, movie.getTitle());
        intent.putExtra(SingleMovieActivity.MOVIE_RELEASE, movie.getReleaseDate());
        intent.putExtra(SingleMovieActivity.MOVIE_PLOT, movie.getPlotSynopsis());
        intent.putExtra(SingleMovieActivity.MOVIE_POSTER, movie.getMoviePoster());
        intent.putExtra(SingleMovieActivity.MOVIE_VOTE, movie.getVoteAverage());
        return intent;
    }
}
