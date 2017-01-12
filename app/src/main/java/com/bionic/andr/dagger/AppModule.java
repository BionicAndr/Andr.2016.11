package com.bionic.andr.dagger;

import com.bionic.andr.AndrApp;

import android.content.Context;

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
