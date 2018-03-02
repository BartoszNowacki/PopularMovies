package com.example.rewan.utils;


import android.util.Log;

public class ImagePathBuilder {
    public String posterPathBuilder(String endpoint){
        final String baseUrl =  "http://image.tmdb.org/t/p/w780";
        return baseUrl+endpoint;
    }
    public String youTubeImagePathBuilder(String id){
        final String baseUrl =  "https://img.youtube.com/vi/";
        final String endUrl = "/0.jpg";
        Log.d("tbs", "youTubeImagePathBuilder: " + baseUrl + id +endUrl);
        return baseUrl+id+endUrl;
    }
}
