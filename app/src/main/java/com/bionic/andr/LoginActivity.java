package com.bionic.andr;

import com.bionic.andr.db.model.Person;
import com.f2prateek.rx.receivers.RxBroadcastReceiver;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.trello.rxlifecycle.components.RxActivity;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.subscriptions.CompositeSubscription;

/**  */
public class LoginActivity extends RxAppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.BLUETOOTH_ADMIN
    };
    public static final int PERSMISSION_REQUEST_CODE = 154;

    @BindView(R.id.login_email)
    EditText email;
    @BindView(R.id.login_pass)
    EditText password;
    @BindView(R.id.login_submit)
    View submit;

    CompositeSubscription subscriptions = new CompositeSubscription();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Observable<Boolean> emailValid = RxTextView.textChanges(email)
                .map(charSequence -> isEmailValid(charSequence));
        Observable<Boolean> passValid = RxTextView.textChanges(password)
                .map(charSequence -> isPasswordValid(charSequence));
        Observable<Boolean> validForm = Observable.combineLatest(emailValid, passValid,
                (email, password) -> email && password);
        validForm.subscribe(valid -> {
            submit.setEnabled(valid);
        });

        RxView.clicks(submit).subscribe(aVoid -> {
            CharSequence emailText = email.getText();
            CharSequence passText = password.getText();
            Toast.makeText(getApplicationContext(), "Login: " + emailText + ":" + passText,
                    Toast.LENGTH_SHORT).show();
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_LOW);
        Subscription lowBatterySubscription = RxBroadcastReceiver.create(this, intentFilter)
                .compose(bindToLifecycle())
                .subscribe(intent -> {
                    Toast.makeText(getApplicationContext(), "Low battery", Toast.LENGTH_SHORT).show();
                });

        subscriptions.add(lowBatterySubscription);



        /*
        StorIOSQLite storIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(null)
                .addTypeMapping(Person.class, SQLiteTypeMapping.<Person>builder()
                        .putResolver(new PersonStorIOSQLitePutResolver()) // object that knows how to perform Put Operation (insert or update)
                        .getResolver(new PersonStorIOSQLiteGetResolver()) // object that knows how to perform Get Operation
                        .deleteResolver(new PersontStorIOSQLiteDeleteResolver())  // object that knows how to perform Delete Operation
                        .build())
        // other options
        .build();
        */

        /*
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i,
                    int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                submit.setEnabled(isValidForm());
            }
        };
        email.addTextChangedListener(watcher);
        password.addTextChangedListener(watcher);

        submit.setEnabled(false);
        */
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!checkPermission()) {
            //finish();
        }
    }

    private boolean checkPermission() {
        List<String> required = new ArrayList<>();
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                required.add(permission);
            }
        }
        if (!required.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    required.toArray(new String[required.size()]),
                    PERSMISSION_REQUEST_CODE);
        }
        return required.isEmpty();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
            @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERSMISSION_REQUEST_CODE) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    //
                    finish();
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        subscriptions.unsubscribe();
//        subscriptions.clear();
    }

    /*
    @OnClick(R.id.login_submit)
    void login() {
        if (!isValidForm()) {
            return;
        }
        CharSequence emailText = email.getText();
        CharSequence passText = password.getText();
        Toast.makeText(getApplicationContext(), "Login: " + emailText + ":" + passText,
                Toast.LENGTH_SHORT).show();
    }
    */

    private boolean isValidForm() {
        CharSequence emailText = email.getText();
        if (!isEmailValid(emailText)) {
            return false;
        }
        CharSequence passText = password.getText();
        if (!isPasswordValid(passText)) {
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(CharSequence passText) {
        return !TextUtils.isEmpty(passText);
    }

    private boolean isEmailValid(CharSequence emailText) {
        return !TextUtils.isEmpty(emailText) && Patterns.EMAIL_ADDRESS.matcher(emailText).matches();
    }

}
