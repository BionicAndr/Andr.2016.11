package com.bionic.andr;

import com.bionic.andr.api.data.Weather;
import com.bionic.andr.core.UpdateService;
import com.bionic.andr.dagger.PrefModule;
import com.bionic.andr.dagger.SensorModule;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements UpdateService.UpdateServiceListener,
        LoaderManager.LoaderCallbacks {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_DETAILS = 10050;
    private static final int LOADER_DUMB = 1;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.pager)
    ViewPager pager;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private WeatherPageAdapter adapter;

    @Named(PrefModule.AUTH_PREF)
    @Inject
    SharedPreferences localPref;

    private Handler sensorHandler;
    @Inject
    SensorManager sensorManager;
    @Named(SensorModule.ACCEL)
    @Inject
    Sensor accel;

    private final SensorEventListener accelListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            Log.d(TAG, Arrays.toString(sensorEvent.values));
            Log.d(TAG, (Looper.getMainLooper() == Looper.myLooper() ? "MAIN THREAD!!!" : " OK "));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private UpdateService service;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            UpdateService.LocalBinder binder = (UpdateService.LocalBinder) iBinder;
            service = binder.getService();
            MainActivity.this.onServiceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            service = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndrApp.getAppComponent().plusActivityComponent().inject(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        adapter = new WeatherPageAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        Intent intent = new Intent(this, UpdateService.class);
        bindService(intent, connection, Service.BIND_AUTO_CREATE);

        if (BuildConfig.LOGGING) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("test", "main")
                    .apply();
        }

        localPref.edit().putString("city", "Lviv").apply();

        getSupportLoaderManager().initLoader(LOADER_DUMB, null, this);


        final Intent i = getIntent();
        final Bundle extras = i.getExtras();
        if (extras != null) {
            ScrollingActivity.Person p = extras.getParcelable("person");
            Log.d("parcelable test", "" + p.getName() + " / " + p.getAge());
        }

        if (BuildConfig.LOGGING) {
            Map<String, ?> prefs = localPref.getAll();
            for (String key : prefs.keySet()) {
                Log.d(TAG, key + " // " + prefs.get(key).toString());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem item = menu.findItem(R.id.action_settings);
//        item.setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SimpleSettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.fab)
    void onFabClicked() {
        final String city = localPref.getString("city", "London");
        service.sync(city, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*
        HandlerThread thread = new HandlerThread("sensor");
        thread.start();
        sensorHandler = new Handler(thread.getLooper());
        sensorManager.registerListener(accelListener, accel,
                SensorManager.SENSOR_DELAY_NORMAL, sensorHandler);
        */
    }

    @Override
    protected void onStop() {
        super.onStop();
        /*
        sensorManager.unregisterListener(accelListener);
        sensorHandler.getLooper().quit();
        if (service != null) {
            unbindService(connection);
        }
        */
    }

    private void onServiceConnected() {
        service.sync("London", this);
    }


    @Override
    public void onDataSynchronized(Weather weather) {
        adapter.add(weather);
        adapter.notifyDataSetChanged();
    }

    @Override
    public Loader onCreateLoader(final int id, final Bundle args) {
        if (id == LOADER_DUMB) {
            return new MyLoader(this);
        }
        return null;
    }

    @Override
    public void onLoadFinished(final Loader loader, final Object data) {
        if (data != null) {
            Toast.makeText(getApplicationContext(), "Loader result: " + data, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(final Loader loader) {
    }

    private class WeatherPageAdapter extends FragmentPagerAdapter {

        private List<Fragment> cache = new ArrayList<>();

        public WeatherPageAdapter(FragmentManager fm) {
            super(fm);
        }

        public void add(Weather weather) {
            WeatherFragment f = new WeatherFragment();
            f.setWeather(weather);
            cache.add(f);
        }

        @Override
        public Fragment getItem(final int position) {
            return cache.get(position);
        }

        @Override
        public int getCount() {
            return cache.size();
        }
    }

    public static class MyLoader extends AsyncTaskLoader<Integer> {

        public MyLoader(Context context) {
            super(context);
        }

        @Override
        public Integer loadInBackground() {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
            }
            return 500;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }
    }


}
