package com.bionic.andr.dagger;

import com.bionic.andr.AndrApp;
import com.bionic.andr.MainActivity;
import com.bionic.andr.SplashActivity;

import android.content.Context;
import android.content.Intent;

import dagger.Module;
import dagger.Provides;

/**  */
@Module
public class AppModule {

    private AndrApp app;

    public AppModule(AndrApp app) {
        this.app = app;
    }

    @Provides
    public Context getContext() {
        return app;
    }

    @Provides
    public AndrApp getApp() {
        return app;
    }

}
