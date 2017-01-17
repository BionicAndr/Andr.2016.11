package com.bionic.andr;

import com.bionic.andr.dagger.AppComponent;
import com.bionic.andr.rx.RxHelloWorld;

import android.app.Application;

/**  */
public class AndrApp extends Application {

    private static AndrApp app;

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        component = AppComponent.Initializer.init(this);
    }

    public static AppComponent getAppComponent() {
        return app.component;
    }

}
