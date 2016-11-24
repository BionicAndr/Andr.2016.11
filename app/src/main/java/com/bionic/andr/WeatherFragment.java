package com.bionic.andr;

import com.bionic.andr.api.data.Weather;
import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**  */

public class WeatherFragment extends Fragment {

    @BindView(R.id.weather_city)
    TextView city;
    @BindView(R.id.weather_icon)
    ImageView icon;
    @BindView(R.id.weather_temp)
    TextView temp;
    @BindView(R.id.weather_min_temp)
    TextView tempMin;
    @BindView(R.id.weather_max_temp)
    TextView tempMax;

    Weather weather;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setWeather(weather);
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
        if (weather == null) {
            return;
        }
        if (city == null) {
            return;
        }
        city.setText(weather.getName());


        temp.setText(getString(R.string.weather_current_template,
                Float.toString(weather.getTemp().getCurrent()),
                weather.getConditions().get(0).getDecr()));
        tempMin.setText(getString(R.string.weather_min_template,
                Float.toString(weather.getTemp().getMin())));
        tempMax.setText(getString(R.string.weather_max_template,
                Float.toString(weather.getTemp().getMax())));

        Picasso.with(getActivity())
                .load(weather.getIconUrl())
                .into(icon);
    }

}
