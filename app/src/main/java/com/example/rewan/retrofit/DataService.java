package com.example.rewan.retrofit;

import com.google.gson.JsonObject;

import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Path;

/**
 * Interface for Retrofit requests
 */

public interface DataService {

    @GET("/3/movie/popular/?api_key={api_key}")
    Call<JsonObject> loadPopularMovies(@Path(value = "api_key", encoded = true) String apiKey);

    @GET("/3/movie/top_rated/?api_key={api_key}")
    Call<JsonObject> loadTopMovies(@Path(value = "api_key", encoded = true) String apiKey);
}