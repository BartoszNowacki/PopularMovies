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

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Defines table and column names for the weather database. This class is not necessary, but keeps
 * the code organized.
 */
public class MovieContract {


    public static final String CONTENT_AUTHORITY = "com.example.rewan";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static final String TABLE_NAME = "movie";


        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE = "release";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_VOTE= "vote";
        public static final String COLUMN_PLOT= "plot";
        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static Uri buildMovieUriWithId(int id){
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(id))
                    .build();
        }
        public static Uri buildMovieUriWithMovieId(String movieId){
            return CONTENT_URI.buildUpon()
                    .appendPath(movieId)
                    .build();
        }
    }

}