package com.example.rewan.base;

import android.support.annotation.StringRes;


public interface MvpView {

    void showMessage(@StringRes int messageId);
    void showErrorMessage(String errorMessage);

}
