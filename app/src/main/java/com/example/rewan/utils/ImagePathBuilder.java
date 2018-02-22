package com.example.rewan.utils;



public class ImagePathBuilder {
    public String pathBuilder(String endpoint){
        final String baseUrl =  "http://image.tmdb.org/t/p/w185";
        return baseUrl+endpoint;
    }
}
