package com.example.rewan.ui.detail;


import com.example.rewan.base.MvpPresenter;
import com.example.rewan.base.MvpView;

public interface DetailContract {
    interface View extends MvpView {
        void setView();
    }

    interface Presenter<V extends MvpView> extends MvpPresenter<V> {
        String getTitle();
        String getPosterPath(String endpoint);
        String getPoster();
        String getPlot();
        String getRelease();
        String getVote();
    }
}
