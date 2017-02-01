package com.bionic.andr.mvp.login;

import com.bionic.andr.api.OpenWeatherApi;
import com.bionic.andr.dagger.AppComponent;
import com.bionic.andr.dagger.NonConfigurationScope;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

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

    private OpenWeatherApi api;

    @Inject
    public LoginPresenter(OpenWeatherApi api) {
//        Log.d(TAG, "NEW " + this);
        this.api = api;
    }

    public void attach(LoginView view) {
        if (this.view != null) {
//            Log.w(TAG, "View leaked!!! " + this.view + " from " + this);
            detach();
        }

//        Log.d(TAG, "attach " + view + " to " + this);

        this.view = view;
       /* Observable<LoginData> validation = Observable.combineLatest(
                view.emailChange(),
                view.passwordChange(),
                (email, password) -> new LoginData(email, password)
        )
        .doOnNext(loginData -> validate(loginData));*/

        Observable<CharSequence> validation = view.chooseCity()
                .doOnNext(cityName -> validate(cityName));


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

    private void login(CharSequence cityName) {
        view.showProgress(true);
        api.getWeatherByCity(getCityName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weather -> {
                    view.showProgress(false);
                    view.onWeatherLoaded(weather);
                },
                t -> {
                    view.showProgress(false);
                    HttpException ex = (HttpException) t;
                    view.onError(ex.code());
                });
    }

    private String getCityName() {
        return view.cityName();
    }

    private void validate(CharSequence cityName) {
        Observable.just(cityName)
                .map(login -> isCityValid(cityName))
                .subscribe(valid -> {
                    view.onValidationCheck(valid);
                });
    }



    private boolean isCityValid(CharSequence cityText) {
        return cityText.length() > 1;
    }




}
