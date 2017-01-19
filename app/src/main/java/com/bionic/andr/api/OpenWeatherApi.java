package com.bionic.andr.api;

import com.bionic.andr.BuildConfig;
import com.bionic.andr.api.data.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**  */

public interface OpenWeatherApi {

//    @GET("data/2.5/weather?appid=" + BuildConfig.API_TOKEN)
    @GET("data/2.5/weather")
    Observable<Weather> getWeatherByCity(@Query("q") String city);

}
