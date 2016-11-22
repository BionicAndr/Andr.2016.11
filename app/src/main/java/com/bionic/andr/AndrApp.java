package com.bionic.andr;

import com.bionic.andr.api.OpenWeatherApi;

import android.app.Application;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**  */
public class AndrApp extends Application {

    private static AndrApp app;

    private OpenWeatherApi api;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(OpenWeatherApi.class);
    }

    public static AndrApp getInstance() {
        return app;
    }

    public OpenWeatherApi getApi() {
        return api;
    }

}
