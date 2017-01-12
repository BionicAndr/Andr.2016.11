package com.bionic.andr.dagger;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**  */
@Module(includes = AppModule.class)
public class SensorModule {

    public static final String ACCEL = "accel";

    @Named(ACCEL)
    @Provides
    public Sensor getAccelerometer(SensorManager manager) {
        return manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Provides
    public SensorManager getSensorManager(Context context) {
        return (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

}
