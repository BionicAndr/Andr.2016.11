package com.bionic.andr;

import com.bionic.andr.api.OpenWeatherApi;
import com.bionic.andr.dagger.AppComponent;
import com.bionic.andr.dagger.DaggerAppComponent;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import android.app.Application;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
