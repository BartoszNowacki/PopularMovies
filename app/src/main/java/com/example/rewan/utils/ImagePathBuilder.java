package com.example.rewan.utils;



public class ImagePathBuilder {
    public String pathBuilder(String endpoint){
        final String baseUrl =  "http://image.tmdb.org/t/p/w780";
        return baseUrl+endpoint;
    }
}
