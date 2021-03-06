package com.example.rewan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Model for Movie reviews feed
 */

public class Review {
    @SerializedName("author")
    @Expose
    private String reviewAuthor;
    @SerializedName("content")
    @Expose
    private String reviewContent;


    public String getReviewAuthor() {
        return reviewAuthor;
    }
    public String getReviewContent() {
        return reviewContent;
    }

    @Override
    public String toString() {
        return "Video{" +
                "author='" + reviewAuthor + '\'' +
                ", content='" + reviewContent + '\'' +
                '}';
    }
    
}
