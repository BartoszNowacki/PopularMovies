package com.example.rewan.Retrofit;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.Call;

/**
 * Interface for Retrofit requests
 * @param
 */

public interface DataService {

    @GET("/country/get/all")
    Call<JsonObject> loadCountries();
}