package com.bionic.andr;

import com.bionic.andr.api.data.Weather;
import com.bionic.andr.core.UpdateService;
import com.squareup.picasso.Picasso;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements UpdateService.UpdateServiceListener {

    private static final int REQUEST_DETAILS = 10050;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.pager)
    ViewPager pager;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private WeatherPageAdapter adapter;

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
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        adapter = new WeatherPageAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        Intent intent = new Intent(this, UpdateService.class);
        bindService(intent, connection, Service.BIND_AUTO_CREATE);
    }

    @OnClick(R.id.fab)
    void onFabClicked() {
        service.sync("Kiev", this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
    }

    private void onServiceConnected() {
        service.sync("London", this);
    }


    @Override
    public void onDataSynchronized(Weather weather) {
        adapter.add(weather);
        adapter.notifyDataSetChanged();
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


}
