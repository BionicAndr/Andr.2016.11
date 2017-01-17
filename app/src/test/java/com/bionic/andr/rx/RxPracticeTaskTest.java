package com.bionic.andr.rx;

import org.junit.Assert;
import org.junit.Test;

import android.widget.ListView;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

public class RxPracticeTaskTest {


    @Test
    public void testPractice1NonNull() {
        Observable<Integer> observable = RxPracticeTask.practice1(getCities());
        Assert.assertNotNull(observable);
    }

    @Test
    public void testPractice1Value() {
        Observable<Integer> observable = RxPracticeTask.practice1(getCities());
        TestSubscriber<Integer> subscriber = new TestSubscriber<>();
        observable.subscribe(subscriber);

        subscriber.assertCompleted();
        subscriber.assertNoErrors();
        subscriber.assertValueCount(2);
        subscriber.assertValues(6, 4);
    }

    @Test
    public void testPractice2NonNull() {
        Observable<BigInteger> observable = RxPracticeTask.practice2();
        Assert.assertNotNull(observable);
    }

    @Test
    public void testPractice2Value() {
        BigInteger result = getCheckValue();

        Observable<BigInteger> observable = RxPracticeTask.practice2();
        TestSubscriber<BigInteger> subscriber = new TestSubscriber<>();
        observable.subscribe(subscriber);

        subscriber.assertCompleted();
        subscriber.assertNoErrors();
        subscriber.assertValueCount(1);
        subscriber.assertValue(result);
    }

    @Test
    public void testPractice2Performance() {
        BigInteger result = getCheckValue();

        Observable<BigInteger> observable = RxPracticeTask.practice2();
        TestSubscriber<BigInteger> subscriber = new TestSubscriber<>();

        long start1 = System.currentTimeMillis();
        observable.subscribe(subscriber);
        long diff1 = System.currentTimeMillis() - start1;

        subscriber.assertCompleted();
        subscriber.assertNoErrors();
        subscriber.assertValueCount(1);
        subscriber.assertValue(result);

        long start2 = System.currentTimeMillis();
        observable.subscribe(subscriber);
        long diff2 = System.currentTimeMillis() - start2;

        subscriber.assertCompleted();
        subscriber.assertNoErrors();
        subscriber.assertValueCount(1);
        subscriber.assertValue(result);
        Assert.assertTrue(diff1 / 4 > diff2);
    }

    @Test
    public void testPractice3NonNull() {
        List<List<Person>> parents = getParents();
        Observable<Person> observable = RxPracticeTask.practice3(
                parents.get(0),
                parents.get(1),
                parents.get(2),
                parents.get(3)
        );
        Assert.assertNotNull(observable);
    }

    @Test
    public void testPractice3Value() {
        List<List<Person>> parents = getParents();
        Observable<Person> observable = RxPracticeTask.practice3(
                parents.get(0),
                parents.get(1),
                parents.get(2),
                parents.get(3)
                );

        TestSubscriber<Person> subscriber = new TestSubscriber<>();
        observable.subscribe(subscriber);

        subscriber.assertCompleted();
        subscriber.assertNoErrors();
        subscriber.assertValueCount(3);
    }

    private static List<String> getCities() {
        final List<String> result = new ArrayList<>();
        result.add("kiev");
        result.add("London");
        result.add("lviv");
        return result;
    }

    private static BigInteger getCheckValue() {
        BigInteger result = BigInteger.ONE;
        for (int i = 1; i < 10_000; i++) {
            int c = (i + 45_000) * 3;
            if (c % 2 == 0) {
                continue;
            }
            result = result.multiply(new BigInteger(String.valueOf(c)));
        }
        return result;
    }

    private List<List<Person>> getParents() {
        List<List<Person>> parents = new ArrayList<>(4);
        List<Person> father1 = new ArrayList<>(2);
        father1.add(new Person("Bob", 32));
        father1.add(new Person("David", 28));
        List<Person> father2 = new ArrayList<>(1);
        father2.add(new Person("Peter", 18));
        List<Person> mother1 = new ArrayList<>(1);
        mother1.add(new Person("Marry", 21));
        List<Person> mother2 = new ArrayList<>(2);
        mother2.add(new Person("Kate", 30));
        mother2.add(new Person("Helen", 21));
        mother2.add(new Person("Olga", 21));
        parents.add(father1);
        parents.add(mother1);
        parents.add(father2);
        parents.add(mother2);
        return parents;
    }

}