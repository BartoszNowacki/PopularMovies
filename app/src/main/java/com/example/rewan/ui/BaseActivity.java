package com.example.rewan.ui;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;

import com.example.rewan.data.MovieContract;

/**
     * Base class for activities
     */
public class BaseActivity extends AppCompatActivity {

    public static final String[] MOVIE_LIST_PROJECTION = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_RELEASE,
            MovieContract.MovieEntry.COLUMN_POSTER,
            MovieContract.MovieEntry.COLUMN_BACKDROP,
            MovieContract.MovieEntry.COLUMN_VOTE,
            MovieContract.MovieEntry.COLUMN_PLOT,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
    };
    public static final int INDEX_MOVIE_TITLE = 1;
    public static final int INDEX_MOVIE_RELEASE = 2;
    public static final int INDEX_MOVIE_POSTER = 3;
    public static final int INDEX_MOVIE_BACKDROP = 4;
    public static final int INDEX_MOVIE_VOTE = 5;
    public static final int INDEX_MOVIE_PLOT = 6;
    public static final int INDEX_MOVIE_ID = 7;

    public static final String POSITION = "position";

    /**
     * Method to check if screen is in Landscape mode
     * @return boolean isLandscapeMode
     */
    public boolean isLandscapeMode(){
        int configuration = getResources().getConfiguration().orientation;
        return configuration == Configuration.ORIENTATION_LANDSCAPE;
}
}
