package com.bionic.andr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;

/**  */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Observable.interval(3, TimeUnit.SECONDS)
                .first()
                .subscribe(Long -> {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                });
    }
}
