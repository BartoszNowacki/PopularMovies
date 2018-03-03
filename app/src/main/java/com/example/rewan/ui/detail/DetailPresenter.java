package com.example.rewan.ui.detail;


import android.content.IntentFilter;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.rewan.base.BasePresenter;
import com.example.rewan.model.BaseModel;
import com.example.rewan.model.Movie;
import com.example.rewan.model.Review;
import com.example.rewan.model.Video;
import com.example.rewan.network.NetworkHelper;
import com.example.rewan.network.NetworkStateDataListener;
import com.example.rewan.retrofit.DataService;
import com.example.rewan.utils.CallType;
import com.example.rewan.utils.DateConverter;
import com.example.rewan.utils.ImagePathBuilder;
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


class DetailPresenter
        extends BasePresenter<DetailContract.View>
        implements DetailContract.Presenter<DetailContract.View>, NetworkStateDataListener {

    private final String API_KEY;
    private NetworkHelper networkHelper;
    private DataService dataService;
    private String LANG;
    private String id;
    private final String TAG = "DetailPresenter";

    DetailPresenter(DataService dataService, String API_KEY){
        Log.d(TAG, "DetailPresenter: ok");
        networkHelper = new NetworkHelper(this);
        this.dataService = dataService;
        this.API_KEY = API_KEY;
        LANG = Locale.getDefault().getLanguage();
    }

    public String getTitle(){
        return Movie.MovieTags.MOVIE_TITLE;
    }
    public String getPosterPath(String endpoint){
        ImagePathBuilder imagePathBuilder = new ImagePathBuilder();
        return imagePathBuilder.posterPathBuilder(endpoint);
    }
    public String getReleaseYear(String date){
        DateConverter dateConverter = new DateConverter();
        return dateConverter.convertDate(date);
    }
    public String getPoster(){
        return Movie.MovieTags.MOVIE_POSTER;
    }
    public String getPlot(){
        return Movie.MovieTags.MOVIE_PLOT;
    }
    public String getRelease(){
        return Movie.MovieTags.MOVIE_RELEASE;
    }
    public String getVote(){
        return Movie.MovieTags.MOVIE_VOTE;
    }
    public String getID(){
        return Movie.MovieTags.ID;
    }
    public String getTransition(){
        return Movie.MovieTags.TRANSITION;
    }
    public void setID(String id){
        this.id = id;
    }

    @Override
    public void makeCall() {
        Call<JsonObject> videosCall = dataService.loadVideos(id, API_KEY, LANG);
        makeCallWithType(videosCall, CallType.VIDEO);
        Call<JsonObject> reviewsCall = dataService.loadReviews(id, API_KEY);
        makeCallWithType(reviewsCall, CallType.REVIEW);
    }

    private void makeCallWithType (Call<JsonObject> typeCall, final CallType callType){
        typeCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonArray moviesJsonArray = response.body().getAsJsonArray("results");
                    if (isViewAttached()) {
                        switch(callType){
                            case VIDEO:
                                Log.d(TAG, "onResponse: tbs video");
                                videosResponse(moviesJsonArray);
                                break;
                            case REVIEW:
                                Log.d(TAG, "onResponse: tbs review");
                                reviewsResponse(moviesJsonArray);
                                break;
                        }
                    }
                } else {
                    int httpCode = response.code();
                    Log.d(TAG, "onResponse: error" + httpCode);
                    view.showErrorMessage(String.valueOf(httpCode));
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                view.showErrorMessage(t.getMessage());
            }
        });
    }
    private void videosResponse(JsonArray videosJsonArray){
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Video>>() {
        }.getType();
        view.setVideosAdapter((List<Video>) gson.fromJson(videosJsonArray, listType));
    }
    private void reviewsResponse(JsonArray reviewsJsonArray){
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Review>>() {
        }.getType();
        view.setReviewsAdapter((List<Review>) gson.fromJson(reviewsJsonArray, listType));
    }
    public IntentFilter getIntentFilter() {
        return networkHelper.getIntentFilter();
    }

    public NetworkHelper getReceiver() {
        return networkHelper;
    }

    public float convertToFloat(String vote){
        return Float.parseFloat(vote)/2;
    }

}
