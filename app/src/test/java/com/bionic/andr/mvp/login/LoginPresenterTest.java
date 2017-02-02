package com.bionic.andr.mvp.login;

import com.bionic.andr.api.data.Weather;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import rx.Observable;

import static org.junit.Assert.*;

/**  */
public class LoginPresenterTest {

    @Test
    public void testValidation() {
        final CountDownLatch latch = new CountDownLatch(1);

        LoginView view = new LoginView() {
            @Override
            public Observable<CharSequence> emailChange() {
                return Observable.just("");
            }

            @Override
            public Observable<CharSequence> passwordChange() {
                return Observable.just("sdfsdfsd");
            }

            @Override
            public Observable<Void> tryToLogin() {
                return Observable.never();
            }

            @Override
            public void onValidationCheck(boolean valid) {
                Assert.assertTrue("Login input is not valid", valid);
                latch.countDown();
            }

            @Override
            public void showProgress(boolean show) {
            }

            @Override
            public void onWeatherLoaded(Weather weather) {
            }

            @Override
            public void onError(int status) {
            }

            @Override
            public void openMainScreen() {

            }
        };
        LoginPresenter presenter = new LoginPresenter();

        presenter.attach(view);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        presenter.detach();
    }

}