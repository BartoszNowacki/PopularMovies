package com.example.rewan.utils;


import android.util.Log;

/**
     * Class for making correct url for images
     */

public class ImagePathBuilder {
    
    /**
     * Method for create url for tmdb.org images
     */
    public String posterPathBuilder(String endpoint){
        final String baseUrl =  "http://image.tmdb.org/t/p/w780";
        return baseUrl+endpoint;
    }
    
     /**
     * Method for create url for youtube.com images
     */
    public String youTubeImagePathBuilder(String id){
        final String baseUrl =  "https://img.youtube.com/vi/";
        final String endUrl = "/0.jpg";
        Log.d("tbs", "youTubeImagePathBuilder: " + baseUrl + id +endUrl);
        return baseUrl+id+endUrl;
    }
}
