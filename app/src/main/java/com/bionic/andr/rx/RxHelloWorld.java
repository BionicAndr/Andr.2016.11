package com.bionic.andr.rx;

import android.util.Log;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class RxHelloWorld {
    private static final String TAG = RxHelloWorld.class.getSimpleName();

    public static void helloWorldNative() {
        String[] arr = new String[] {"Hello", "World"};
        for (String s : arr) {
            Log.d(TAG, s);
        }
    }

    public static void helloWorld1() {
        Observable<String> myObservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> sub) {
                        sub.onNext("Hello");
                        sub.onNext("World");
                        sub.onCompleted();
                    }
                }
        );
        Subscriber<String> mySubscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) { Log.d(TAG, s); }

            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable e) { }
        };
        myObservable.subscribe(mySubscriber);
    }

    public static void helloWorld2() {
        Observable<String> myObservable = Observable.just("Hello", "World");
        Subscriber<String> mySubscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) { Log.d(TAG, s); }

            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable e) { }
        };
        myObservable.subscribe(mySubscriber);
    }

    public static void helloWorld3() {
        Observable<String> myObservable = Observable.just("Hello", "World");
        Action1<String> mySubscriber = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG, s);
            }
        };
        myObservable.subscribe(mySubscriber);
    }

    public static void helloWorld4() {
        Observable<String> myObservable = Observable.just("Hello", "World");
        Action1<String> mySubscriber = s -> Log.d(TAG, s);
        myObservable.subscribe(mySubscriber);
    }

}
