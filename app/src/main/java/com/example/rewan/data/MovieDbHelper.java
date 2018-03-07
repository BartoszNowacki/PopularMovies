package com.example.rewan.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.rewan.data.MovieContract.MovieEntry;

/**
 * Manages a local database for movie data.
 */
public class MovieDbHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "movie.db";


    private static final int DATABASE_VERSION = 1;

    MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE =

                "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +


                        MovieEntry._ID                + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MovieEntry.COLUMN_TITLE       + " TEXT NOT NULL, "                 +
                        MovieEntry.COLUMN_RELEASE     + " TEXT NOT NULL,"                  +
                        MovieEntry.COLUMN_POSTER      + " TEXT NOT NULL, "                 +
                        MovieEntry.COLUMN_BACKDROP    + " TEXT NOT NULL, "                 +
                        MovieEntry.COLUMN_VOTE        + " TEXT NOT NULL,"                  +
                        MovieEntry.COLUMN_PLOT        + " TEXT NOT NULL,"                  +
                        MovieEntry.COLUMN_MOVIE_ID    + " TEXT NOT NULL,"                  +
                        " UNIQUE (" + MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";


        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
