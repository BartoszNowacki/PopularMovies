package com.example.rewan.ui.detail;


import com.example.rewan.base.MvpPresenter;
import com.example.rewan.base.MvpView;
import com.example.rewan.model.Review;
import com.example.rewan.model.Video;

import java.util.List;

public interface DetailContract {
    interface View extends MvpView {
        void setView();
        void setVideosAdapter(List<Video> videos);
        void setReviewsAdapter(List<Review> reviews);
    }

    interface Presenter<V extends MvpView> extends MvpPresenter<V> {
        String getTitle();
        String getPosterPath(String endpoint);
        String getPoster();
        String getPlot();
        String getRelease();
        String getVote();
        float convertToFloat(String vote);
    }
}
