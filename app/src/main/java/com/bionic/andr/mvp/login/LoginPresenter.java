package com.bionic.andr.mvp.login;

import com.bionic.andr.api.OpenWeatherApi;
import com.bionic.andr.dagger.AppComponent;
import com.bionic.andr.dagger.NonConfigurationScope;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**  */
@NonConfigurationScope
public class LoginPresenter {
    /** Log tag. */
    private static final String TAG = LoginPresenter.class.getSimpleName();

    private LoginView view;

    @Inject
    OpenWeatherApi api;

    @Inject
    public LoginPresenter() {
//        Log.d(TAG, "NEW " + this);
    }

    public void attach(LoginView view) {
        if (this.view != null) {
//            Log.w(TAG, "View leaked!!! " + this.view + " from " + this);
            detach();
        }

//        Log.d(TAG, "attach " + view + " to " + this);

        this.view = view;
        Observable<LoginData> validation = Observable.combineLatest(
                view.emailChange(),
                view.passwordChange(),
                (email, password) -> new LoginData(email, password)
        )
        .doOnNext(loginData -> validate(loginData));

        view.tryToLogin()
                .withLatestFrom(validation, (aVoid, loginData) -> loginData)
                .subscribe(loginData -> {
                    login(loginData);
                });
    }

    public void detach() {
//        Log.d(TAG, "detach " + view + " from " + this);
        this.view = null;
    }

    private void login(LoginData loginData) {
        view.showProgress(true);
        Observable.timer(2, TimeUnit.SECONDS)
        //api.getWeatherByCity("Lviv")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weather -> {
                    view.showProgress(false);
                    //view.onWeatherLoaded(weather);
                    view.openMainScreen();
                },
                t -> {
                    view.showProgress(false);
                    //HttpException ex = (HttpException) t;
                    //view.onError(ex.code());
                });
    }

    private void validate(LoginData loginData) {
        Observable.just(loginData)
                .map(login -> isEmailValid(login.email) && isPasswordValid(login.password))
                .subscribe(valid -> {
                    view.onValidationCheck(valid);
                });
    }

    private boolean isPasswordValid(CharSequence passText) {
        return passText != null && passText.length() > 0;
    }

    private boolean isEmailValid(CharSequence emailText) {
        //TODO: write email pattern
        return emailText != null && emailText.length() > 0;// && Patterns.EMAIL_ADDRESS.matcher(emailText).matches();
    }


    private static class LoginData {
        private final CharSequence email, password;

        private LoginData(CharSequence email, CharSequence password) {
            this.email = email;
            this.password = password;
        }
    }

}
