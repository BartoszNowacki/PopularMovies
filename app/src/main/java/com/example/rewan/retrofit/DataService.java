package com.example.rewan.retrofit;

import com.google.gson.JsonObject;

import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface for Retrofit requests
 */

public interface DataService {

    @GET("/3/movie/popular")
    Call<JsonObject> loadPopularMovies(@Query("api_key") String api_key, @Query("language") String lang);

    @GET("/3/movie/top_rated")
    Call<JsonObject> loadTopMovies(@Query("api_key") String api_key, @Query("language") String lang);

    @GET("/3/movie/{id}/videos")
    Call<JsonObject> loadVideos(@Path(value = "id", encoded = true) String movieID, @Query("api_key") String api_key);

    @GET("/3/movie/{id}/reviews")
    Call<JsonObject> loadReviews(@Path(value = "id", encoded = true) String movieID, @Query("api_key") String api_key);
}