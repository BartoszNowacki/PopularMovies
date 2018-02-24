package com.example.rewan.ui.main;

import com.example.rewan.base.MvpPresenter;
import com.example.rewan.base.MvpView;

import java.util.List;

/**
 * Created by Rewan on 24.02.2018.
 */

public interface MainContract {

    interface View extends MvpView {
    }

    interface Presenter<V extends MvpView> extends MvpPresenter<V> {
    }
}