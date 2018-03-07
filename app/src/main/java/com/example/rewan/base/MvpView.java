package com.example.rewan.base;

import android.support.annotation.StringRes;
/**
 * Base interface for all MVP Views in app
 */

public interface MvpView {

    void showMessage(@StringRes int messageId);
    void showErrorMessage(String errorMessage);

}
