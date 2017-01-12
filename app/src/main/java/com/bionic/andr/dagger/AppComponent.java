package com.bionic.andr.dagger;

import com.bionic.andr.AndrApp;
import com.bionic.andr.core.UpdateService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        NetworkModule.class,
        RestModule.class,
        ImageLoadingModule.class,
        PrefModule.class,
        SensorModule.class
})
public interface AppComponent {

    ActivityComponent plusActivityComponent();

    void inject(AndrApp app);
    void inject(UpdateService updateService);

    static final class Initializer {
        private Initializer() {
        }

        public static AppComponent init(AndrApp app) {
            return DaggerAppComponent.builder()
                    .appModule(new AppModule(app))
                    .imageLoadingModule(new ImageLoadingModule())
                    .restModule(new RestModule())
                    .networkModule(new NetworkModule())
                    .build();
        }
    }


}
