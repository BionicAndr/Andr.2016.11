package com.bionic.andr.dagger;

import com.bionic.andr.api.OpenWeatherApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**  */
@Module(includes = {NetworkModule.class})
public class RestModule {

    @Singleton
    @Provides
    public Retrofit getRetrofit(OkHttpClient client, String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    public OpenWeatherApi getApi(Retrofit retrofit) {
        return retrofit.create(OpenWeatherApi.class);
    }

    @Provides
    public String getBaseUrl() {
        return "http://api.openweathermap.org/";
    }

}
