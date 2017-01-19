package com.bionic.andr.db.model;

import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

//@StorIOSQLiteType(table = "person")
public class Person {
    //@StorIOSQLiteColumn(name = "name")
    String name;
    //@StorIOSQLiteColumn(name = "age")
    int age;

    public static Person makeChild(Person father, Person mother) {
        return new Person("Child of " + father.getName() + " and " + mother.getName(), 0);
    }

    public Person() {
        // for StorIO
    }

    public Person(String name, int age) {
        this.age = age;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(final int age) {
        this.age = age;
    }
}
