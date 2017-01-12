package com.bionic.andr.dagger;

import com.bionic.andr.MainActivity;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {})
public interface ActivityComponent {

    FragmentComponent plusFragmentComponent();

    void inject(MainActivity activity);
}
