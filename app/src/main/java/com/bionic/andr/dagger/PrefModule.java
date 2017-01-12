package com.bionic.andr.dagger;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Named;
import javax.inject.Scope;

import dagger.Module;
import dagger.Provides;

/**  */
@Module(includes = AppModule.class)
public class PrefModule {

    public static final String AUTH_PREF = "auth";

    @Provides
    public SharedPreferences getMainPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Named(AUTH_PREF)
    @Provides
    public SharedPreferences getAuthPrefs(Context context) {
        return context.getSharedPreferences(AUTH_PREF, Context.MODE_PRIVATE);
    }

}
