/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.rewan.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.rewan.data.MovieContract.MovieEntry;

/**
 * Manages a local database for weather data.
 */
public class MovieDbHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "movie.db";


    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
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