package com.bionic.andr.dagger;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**  */
@Module(includes = {NetworkModule.class})
public class ImageLoadingModule {

    @Singleton
    @Provides
    public Picasso getPicasso(Context context, OkHttp3Downloader downloader) {
        return new Picasso.Builder(context)
                .downloader(downloader)
                .build();
    }

    @Provides
    public OkHttp3Downloader getDownloader(OkHttpClient client) {
        return new OkHttp3Downloader(client);
    }

}
