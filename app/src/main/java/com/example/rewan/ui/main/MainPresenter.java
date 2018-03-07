package com.example.rewan.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;

import com.example.rewan.R;
import com.example.rewan.base.BasePresenter;
import com.example.rewan.data.MovieContract;
import com.example.rewan.model.Movie;
import com.example.rewan.network.NetworkHelper;
import com.example.rewan.network.NetworkStateDataListener;
import com.example.rewan.retrofit.DataService;
import com.example.rewan.utils.CallType;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

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
    private String LANG;

    MainPresenter(DataService dataService, String API_KEY) {
        networkHelper = new NetworkHelper(this);
        this.dataService = dataService;
        this.API_KEY = API_KEY;
        LANG = Locale.getDefault().getLanguage();
    }

    @Override
    public void getMovies(Context context) {
        if (networkHelper.isNetworkAvailable(context)) {
            makeCall();
        } else {
            view.showMessage(R.string.network_disabled);
        }
    }

    @Override
    public void makeCall() {
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

    private Call<JsonObject> getSortOrder() {
        if (isTopCategory) {
            return dataService.loadTopMovies(API_KEY, LANG);
        } else {
            return dataService.loadPopularMovies(API_KEY, LANG);
        }
    }
    /**
     * Sets variable isTopCategory
     * @param isTopCategory boolean to set category
     */
    void setTopCategory(boolean isTopCategory) {
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

    @Override
    public Intent configuredIntent(Intent intent, Movie movie, int position) {
        intent.putExtra(Movie.MovieTags.MOVIE_TITLE, movie.getTitle());
        intent.putExtra(Movie.MovieTags.MOVIE_RELEASE, movie.getReleaseDate());
        intent.putExtra(Movie.MovieTags.MOVIE_PLOT, movie.getPlotSynopsis());
        intent.putExtra(Movie.MovieTags.MOVIE_POSTER, movie.getMoviePoster());
        intent.putExtra(Movie.MovieTags.MOVIE_BACKDROP, movie.getMovieBackdrop());
        intent.putExtra(Movie.MovieTags.MOVIE_VOTE, movie.getVoteAverage());
        intent.putExtra(Movie.MovieTags.ID, movie.getID());
        return intent;
    }
}
