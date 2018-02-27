package com.example.rewan.ui.detail;


import com.example.rewan.base.BasePresenter;
import com.example.rewan.model.Movie;
import com.example.rewan.utils.ImagePathBuilder;


class DetailPresenter
        extends BasePresenter<DetailContract.View>
        implements DetailContract.Presenter<DetailContract.View>{

    DetailPresenter(){
    }

    public String getTitle(){
        return Movie.MovieTags.MOVIE_TITLE;
    }
    public String getPosterPath(String endpoint){
        ImagePathBuilder imagePathBuilder = new ImagePathBuilder();
        return imagePathBuilder.pathBuilder(endpoint);
    }
    public String getPoster(){
        return Movie.MovieTags.MOVIE_POSTER;
    }
    public String getPlot(){
        return Movie.MovieTags.MOVIE_PLOT;
    }
    public String getRelease(){
        return Movie.MovieTags.MOVIE_RELEASE;
    }
    public String getVote(){
        return Movie.MovieTags.MOVIE_VOTE;
    }
}
