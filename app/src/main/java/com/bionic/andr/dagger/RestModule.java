package com.bionic.andr.dagger;

import com.bionic.andr.api.OpenWeatherApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**  */
@Module(includes = {NetworkModule.class})
public class RestModule {

    @Singleton
    @Provides
    public Retrofit getRetrofit(OkHttpClient client, String baseUrl, CallAdapter.Factory factory) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(factory)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    public OpenWeatherApi getApi(Retrofit retrofit) {
        return retrofit.create(OpenWeatherApi.class);
    }

    @Singleton
    @Provides
    public CallAdapter.Factory getRxCallFactory() {
        return RxJavaCallAdapterFactory.create();
    }

    @Provides
    public String getBaseUrl() {
        return "http://api.openweathermap.org/";
    }

}
