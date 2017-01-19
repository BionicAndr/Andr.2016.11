package com.bionic.andr.rx;

import com.bionic.andr.db.model.Person;

import android.support.annotation.NonNull;

import java.math.BigInteger;
import java.util.List;

import rx.Observable;

public class RxPracticeTask {


    /**
     * Filter out cities with name started from L or l.
     * @param cities city names.
     * @return observable with city name length.
     */
    public static Observable<Integer> practice1(@NonNull List<String> cities) {
        return Observable.from(cities)
                .filter(s -> s.startsWith("L") || s.startsWith("l"))
                .map(s -> s.length());
    }

    /**
     * 1) take range 1..100000
     * 2) multiply all by 3
     * 3) remove 45000 from start and take next 10000
     * 4) remove elements divided by 2
     * 5) transform into BigInteger
     * 6) multiply all values one by one
     * 7) cache
     *
     * @return cached result with single BigInteger
     */
    public static Observable<BigInteger> practice2() {
        return Observable.range(1, 100_000)
                .map(integer -> integer * 3)
                .skip(45_000)
                .take(10_000)
                .filter(i -> i % 2 != 0)
                .map(integer -> BigInteger.valueOf(integer))
                .reduce(BigInteger.ONE, (i1, i2) -> i1.multiply(i2))
                .cache();
    }


    /**
     * 1) Combine fathers into one list
     * 2) Combine mothers into one list
     * 3) Create child within fathers and mothers lists {@link Person#makeChild(Person, Person)}.
     *
     * @return observable of children
     */
    public static Observable<Person> practice3(List<Person> father1,
            List<Person> mother1, List<Person> father2, List<Person> mother2) {
        return Observable.zip(
                Observable.from(father1).concatWith(Observable.from(father2)),
                Observable.from(mother1).concatWith(Observable.from(mother2)),
                (father, mother) -> Person.makeChild(father, mother)
        );
    }

}
