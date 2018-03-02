package com.example.rewan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rewan on 01.03.2018.
 */

public class Video {
    @SerializedName("name")
    @Expose
    private String videoTitle;
    @SerializedName("key")
    @Expose
    private String videoEndpoint;


    public String getVideoTitle() {
        return videoTitle;
    }
    public String getVideoEndpoint() {
        return videoEndpoint;
    }

    @Override
    public String toString() {
        return "Video{" +
                "title='" + videoTitle + '\'' +
                ", key='" + videoEndpoint + '\'' +
                '}';
    }
    
}
