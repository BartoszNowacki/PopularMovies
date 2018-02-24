package com.example.rewan.base;

/**
 * Created by Maciej Bialorucki on 12.12.17.
 */

/**
 * Base interface for all presenters in app
 * @param <V>
 */
public interface MvpPresenter<V extends MvpView> {

    void attachView(V view);
    void detachView();
    boolean isViewAttached();
}
