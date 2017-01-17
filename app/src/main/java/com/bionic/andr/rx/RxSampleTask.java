package com.bionic.andr.rx;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

public class RxSampleTask {

    public static Observable<Integer> map() {
        return Observable.just("Kiev", "Lviv", "London")
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        return s.length();
                    }
                })
                .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        return integer * 2;
                    }
                });
    }

    public static Observable<Integer> first() {
        return Observable.just(1, 2, 4, 8, 16, 32)
                .first(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > 8;
                    }
                });
    }

    public static Observable<Integer> flatMap(Observable<List<String>> listObservable) {
        return listObservable
                .flatMap(new Func1<List<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(final List<String> strings) {
                        return Observable.from(strings);
                    }
                })
                .map(String::length);
    }

    public static Observable<Person> zip() {
Observable<String> namesObservable = Observable.just("Bob", "David", "Marry");
Observable<Integer> ageObservable = Observable.just(34, 32, 21);
return Observable.zip(namesObservable, ageObservable,
        new Func2<String, Integer, Person>() {
            @Override
            public Person call(final String s, final Integer integer) {
                return new Person(s, integer);
            }
        });
    }

}
