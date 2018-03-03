package com.example.rewan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class Video extends BaseModel{
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
