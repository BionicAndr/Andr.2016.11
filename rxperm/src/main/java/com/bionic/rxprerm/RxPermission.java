package com.bionic.rxprerm;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**  */

public class RxPermission {

    public static Observable<PermissionResult> ensure(
            final AppCompatActivity activity, String... permission) {
        return Observable.from(permission)
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return ContextCompat.checkSelfPermission(activity, s)
                                != PackageManager.PERMISSION_GRANTED;
                    }
                })
                .toList()
                .map(new Func1<List<String>, ArrayList<String>>() {
                    @Override
                    public ArrayList<String> call(List<String> strings) {
                        return new ArrayList<>(strings);
                    }
                })
                .flatMap(new Func1<ArrayList<String>, Observable<PermissionResult>>() {
                    @Override
                    public Observable<PermissionResult> call(ArrayList<String> strings) {
                        RxPermissionFragment f = new RxPermissionFragment();
                        Bundle args = new Bundle();
                        args.putStringArrayList(RxPermissionFragment.ARG_PERMISSIONS, strings);
                        f.setArguments(args);

                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .add(f, RxPermissionFragment.TAG)
                                .commit();
                        return f.getObservable();
                    }
                });
    }

    public static class PermissionResult {
        public final String name;
        public final int granded;

        public PermissionResult(String permission, int granded) {
            this.name = permission;
            this.granded = granded;
        }
    }


}
