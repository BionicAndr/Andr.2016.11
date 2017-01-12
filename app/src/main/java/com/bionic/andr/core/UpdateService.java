package com.bionic.andr.core;

import com.bionic.andr.AndrApp;
import com.bionic.andr.BuildConfig;
import com.bionic.andr.R;
import com.bionic.andr.api.OpenWeatherApi;
import com.bionic.andr.api.data.Weather;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**  */
public class UpdateService extends Service {
    private static final String TAG = UpdateService.class.getSimpleName();

    private final LocalBinder binder = new LocalBinder();

    @Inject
    OpenWeatherApi api;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private final Handler mainHadler = new Handler(Looper.getMainLooper());

    @Inject
    SharedPreferences pref;

    @Override
    public void onCreate() {
        super.onCreate();
        AndrApp.getAppComponent().inject(this);
    }

    public void sync(String city, final UpdateServiceListener listener) {
        if (BuildConfig.LOGGING) {
            Map<String, ?> prefs = pref.getAll();
            for (String key : prefs.keySet()) {
                Log.d(TAG, key + " // " + prefs.get(key).toString());
            }
        }

        final boolean sync = pref.getBoolean(getString(R.string.pref_sync_key), true);
        if (!sync) {
            return;
        }
        api.getWeatherByCity(city).enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                Log.d(TAG, "Get weather success: " + response.code());
                if (response.isSuccessful()) {
                    final Weather weather = response.body();
                    Log.d(TAG, "City: " + weather.getName());
                    Log.d(TAG, "Temp: " + weather.getTemp().getCurrent());
                    Log.d(TAG, "Condition: " + weather.getConditions().get(0).getDecr());
                    Log.d(TAG, "Icon: " + weather.getIconUrl());

                    if (listener != null) {
                        listener.onDataSynchronized(weather);
                    }
                }

            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Log.e(TAG, "Get weather failed", t);
            }
        });
    }

            /*
        DbHelper helper = new DbHelper(this);
        SQLiteDatabase wdb = helper.getWritableDatabase();

        ContentValues insertValues = new ContentValues(2);
        insertValues.put(DbContract.Person.NAME, "Bob");
        insertValues.put(DbContract.Person.AGE, 23);

        long result = wdb.insert(DbContract.Person.TABLE, null, insertValues);

        SQLiteDatabase rdb = helper.getReadableDatabase();
        String[] columns = new String[] {
                DbContract.Person.NAME,
                DbContract.Person.AGE
        };

        Cursor c = rdb.query(DbContract.Person.TABLE,
                columns,
                null,
                null,
                null,
                null,
                null
                );

        try {
            int cName = c.getColumnIndex(DbContract.Person.NAME);
            int cAge = c.getColumnIndex(DbContract.Person.AGE);
            while (c.moveToNext()) {
                String name = c.getString(cName);
                int age = c.getInt(cAge);
                Log.d("Database", "Name = " + name + " Age = " + age);
            }
        } finally {
            c.close();
        }
        */

    public interface UpdateServiceListener {
        void onDataSynchronized(Weather weather);
    }

    public class LocalBinder extends Binder {

        public UpdateService getService() {
            return UpdateService.this;
        }

    }

    public static abstract class UpdateServiceBroadcastReciever extends BroadcastReceiver {

        public static final String ACTION = UpdateService.class.getSimpleName() + ":reciever";

        public void register(Context context) {
            IntentFilter filter = new IntentFilter(ACTION);
            context.registerReceiver(this, filter);
        }

        public void unregister(Context context) {
            context.unregisterReceiver(this);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ACTION.equals(action)) {
                onDataUpdated();
            }
        }

        public abstract void onDataUpdated();
    }

}
