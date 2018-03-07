package com.example.rewan.base;


/**
 * Base interface for all presenters in app
 * @param <V>
 */
public interface MvpPresenter<V extends MvpView> {

    void attachView(V view);
    void detachView();
    boolean isViewAttached();
}
