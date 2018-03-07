
package com.example.rewan.data;

import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Defines table and column names for the movie database.
 */
public class MovieContract {


    static final String CONTENT_AUTHORITY = "com.example.rewan";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    static final String PATH_MOVIE = "movie";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        static final String TABLE_NAME = "movie";


        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE = "release";
        public static final String COLUMN_POSTER = "poster";     
        public static final String COLUMN_BACKDROP = "backdrop";
        public static final String COLUMN_VOTE= "vote";
        public static final String COLUMN_PLOT= "plot";
        public static final String COLUMN_MOVIE_ID = "movie_id";

        static Uri buildMovieUriWithId(int id){
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(id))
                    .build();
        }
    }

}
