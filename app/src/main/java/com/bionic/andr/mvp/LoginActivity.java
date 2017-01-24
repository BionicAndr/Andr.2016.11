package com.bionic.andr.mvp;

import com.bionic.andr.AndrApp;
import com.bionic.andr.R;
import com.bionic.andr.api.data.Weather;
import com.bionic.andr.mvp.login.LoginPresenter;
import com.bionic.andr.mvp.login.LoginView;
import com.bionic.andr.ui.ProgressDialogFragment;
import com.f2prateek.rx.receivers.RxBroadcastReceiver;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**  */
public class LoginActivity extends AppCompatActivity implements LoginView {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final String PROGRESS_DIALOG_TAG = "dialog:progress";

    @BindView(R.id.login_email)
    EditText email;
    @BindView(R.id.login_pass)
    EditText password;
    @BindView(R.id.login_submit)
    View submit;

    LoginPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        presenter = new LoginPresenter(AndrApp.getAppComponent());
        presenter.attach(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    @Override
    public Observable<CharSequence> emailChange() {
        return RxTextView.textChanges(email);
    }

    @Override
    public Observable<CharSequence> passwordChange() {
        return RxTextView.textChanges(password);
    }

    @Override
    public Observable<Void> tryToLogin() {
        return RxView.clicks(submit);
    }

    @Override
    public void onValidationCheck(boolean valid) {
        submit.setEnabled(valid);
    }

    @Override
    public void showProgress(boolean show) {
        FragmentManager fm = getSupportFragmentManager();
        ProgressDialogFragment f = (ProgressDialogFragment) fm.findFragmentByTag(PROGRESS_DIALOG_TAG);
        if (show) {
            if (f == null) {
                ProgressDialogFragment.newInstance("Loading")
                        .show(getSupportFragmentManager(), PROGRESS_DIALOG_TAG);
            }
        } else if (f != null) {
            f.dismissAllowingStateLoss();
        }
    }

    @Override
    public void onWeatherLoaded(Weather weather) {
        Toast.makeText(getApplicationContext(), weather.getName() + " / " + weather.getTemp(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(int status) {
        Toast.makeText(getApplicationContext(), "Error: " + status, Toast.LENGTH_SHORT).show();
    }
}
