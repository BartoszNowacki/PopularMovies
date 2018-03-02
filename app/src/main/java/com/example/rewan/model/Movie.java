package com.example.rewan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Model for Movie feed
 */
public class Movie {

        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("release_date")
        @Expose
        private String releaseDate;
        @SerializedName("poster_path")
        @Expose
        private String moviePoster;
        @SerializedName("vote_average")
        @Expose
        private String voteAverage;
        @SerializedName("overview")
        @Expose
        private String plotSynopsis;
        @SerializedName("id")
        @Expose
        private String id;

        public String getTitle() {
            return title;
        }
        public String getReleaseDate() {
            return releaseDate;
        }
        public String getMoviePoster() {
            return moviePoster;
        }
        public String getVoteAverage() {
                return voteAverage;
        }
        public String getPlotSynopsis() {
                return plotSynopsis;
        }
        public String getID() {
                return id;
        }


        @Override
        public String toString() {
            return "Movie{" +
                    "title='" + title + '\'' +
                    ", releaseDate='" + releaseDate + '\'' +
                    ", moviePoster='" + moviePoster + '\'' +
                    ", voteAverage='" + voteAverage + '\'' +
                    ", plotSynopsis='" + moviePoster + '\'' +
                    ", ID='" + id + '\'' +
                    '}';
        }
        public static class MovieTags {
                public final static String MOVIE_TITLE = "title";
                public final static String MOVIE_RELEASE = "release";
                public final static String MOVIE_POSTER = "poster";
                public final static String MOVIE_VOTE = "vote";
                public final static String MOVIE_PLOT = "plot";
                public final static String ID = "id";
        }
}
