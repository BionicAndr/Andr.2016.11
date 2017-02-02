package com.bionic.rxprerm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;

/**  */

public class RxPermissionFragment extends Fragment {

    public static final String TAG = RxPermissionFragment.class.getSimpleName();

    public static final String ARG_PERMISSIONS = "arg:name";
    public static final int PERMISSIONS_REQUEST_CODE = 1;

    private ReplaySubject<RxPermission.PermissionResult> resultSubject = ReplaySubject.create();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        ArrayList<String> permissions = args.getStringArrayList(ARG_PERMISSIONS);
        String[] cast = new String[permissions.size()];
        permissions.toArray(cast);
        requestPermissions(cast, PERMISSIONS_REQUEST_CODE);
    }

    public Observable<RxPermission.PermissionResult> getObservable() {
        return resultSubject;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; ++i) {
            RxPermission.PermissionResult perm = new RxPermission.PermissionResult(
                    permissions[i], grantResults[i]);
            resultSubject.onNext(perm);
        }
        resultSubject.onCompleted();
        dismiss();
    }

    private void dismiss() {
        getFragmentManager().beginTransaction()
                .remove(this)
                .commitAllowingStateLoss();
    }

}
