package com.bionic.andr;

import com.bionic.rxprerm.RxPermission;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;

/**  */
public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        RxPermission.ensure(this, Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Action1<RxPermission.PermissionResult>() {
                               @Override
                               public void call(RxPermission.PermissionResult perm) {
                                   Log.d(TAG, "Permission " + perm.name
                                           + " " + perm.granded);
                               }
                           },
                        t -> {
                        },
                        () -> {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        Observable.interval(3, TimeUnit.SECONDS)
                .first()
                .subscribe(Long -> {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                });
        */
    }
}
