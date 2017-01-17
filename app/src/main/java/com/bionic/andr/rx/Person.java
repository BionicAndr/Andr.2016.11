package com.bionic.andr.rx;

public class Person {

    private String name;

    private int age;

    public static Person makeChild(Person father, Person mother) {
        return new Person("Child of " + father.getName() + " and " + mother.getName(), 0);
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
