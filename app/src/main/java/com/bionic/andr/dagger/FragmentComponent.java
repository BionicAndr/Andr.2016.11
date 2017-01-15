package com.bionic.andr.dagger;

import com.bionic.andr.WeatherFragment;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface FragmentComponent {

    void inject(WeatherFragment fragment);

}
