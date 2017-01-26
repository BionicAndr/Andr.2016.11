package com.bionic.andr.mvp;

import com.bionic.andr.AndrApp;
import com.bionic.andr.dagger.NonConfigurationComponent;
import com.bionic.andr.dagger.NonConfigurationScope;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/** Base activity. */
public abstract class BaseActivity extends AppCompatActivity {

    /** Injects Presenter. */
    private NonConfigurationComponent injector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injector = restoreInjector();
        inject(injector);
    }

    public abstract void inject(NonConfigurationComponent injector);

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
    }

    private NonConfigurationComponent restoreInjector() {
        Object o = getLastCustomNonConfigurationInstance();
        if (o == null) {
            return AndrApp.getAppComponent().plusNonConfiguration();
        } else {
            return (NonConfigurationComponent) o;
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return injector;
    }
}
