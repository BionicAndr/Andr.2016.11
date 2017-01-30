package com.bionic.andr.mvp.login;

import com.bionic.andr.api.OpenWeatherApi;
import com.bionic.andr.dagger.AppComponent;
import com.bionic.andr.mvp.LoginActivity;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.tbruyelle.rxpermissions.RxPermissionsFragment;

import android.Manifest;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**  */
public class LoginPresenter {
    private LoginView view;

    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };
    public static final int PERSMISSION_REQUEST_CODE = 154;

    @Inject
    OpenWeatherApi api;

    public LoginPresenter(AppComponent component) {
        component.inject(this);
    }

    public void attach(LoginView view) {
        this.view = view;
//        Observable<LoginData> validation = Observable.combineLatest(
//                view.emailChange(),
//                view.passwordChange(),
//                (email, password) -> new LoginData(email, password)
//        )
//        .doOnNext(loginData -> validate(loginData));

        Observable<CharSequence> cityNameValidation = view.cityChange()
                .doOnNext(cityName -> validateInputCityName(cityName));

//        view.tryToLogin()
//                .withLatestFrom(cityNameValidation, (aVoid, loginData) -> loginData)
//                .subscribe(loginData -> {
//                    login(loginData);
//                });

        view.tryToLogin()
                .withLatestFrom(cityNameValidation, (aVoid, loginData) -> loginData)
                .subscribe(loginData -> {
                    getWeather(loginData);
                });

        RxPermissions rxPermissions = new RxPermissions((Activity) view);

        view.pickUpLocation()
                .compose(rxPermissions.ensure(PERMISSIONS))
                .subscribe(granted -> {
                    if (granted) {
                        view.setCityName("Kiev");
                        Log.d("PERMISSIONS", "granted");
                    } else  {
                        Log.d("PERMISSIONS", "not granted");
                    }
                });
    }

    public void detach() {
        this.view = null;
    }

    private void login(LoginData loginData) {
        getWeather("Lviv");
    }

    private void getWeather(CharSequence cityName) {
        view.showProgress(true);
        api.getWeatherByCity(cityName.toString())
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

    private void validate(LoginData loginData) {
        Observable.just(loginData)
                .map(login -> isEmailValid(login.email) && isPasswordValid(login.password))
                .subscribe(valid -> {
                    view.onValidationCheck(valid);
                });
    }

    private void validateInputCityName(CharSequence cityName) {
        Observable.just(cityName)
                .map(city -> isCityNameValid(city))
                .subscribe(valid -> {
                    view.onValidationCheck(valid);
        });
    }

    private boolean isCityNameValid(CharSequence city) {
        return !TextUtils.isEmpty(city) && (city.toString().length() > 1);
    }

    private boolean isPasswordValid(CharSequence passText) {
        return !TextUtils.isEmpty(passText);
    }

    private boolean isEmailValid(CharSequence emailText) {
        return !TextUtils.isEmpty(emailText) && Patterns.EMAIL_ADDRESS.matcher(emailText).matches();
    }


    private static class LoginData {
        private final CharSequence email, password;

        private LoginData(CharSequence email, CharSequence password) {
            this.email = email;
            this.password = password;
        }
    }

}
