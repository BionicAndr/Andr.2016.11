package com.bionic.andr.mvp.login;

import com.bionic.andr.api.data.Weather;

import rx.Observable;

/**  */
public interface LoginView {

    Observable<CharSequence> emailChange();
    Observable<CharSequence> passwordChange();
    Observable<Void> tryToLogin();

    void onValidationCheck(boolean valid);
    void showProgress(boolean show);
    void onWeatherLoaded(Weather weather);
    void onError(int status);
    void openMainScreen();
}
