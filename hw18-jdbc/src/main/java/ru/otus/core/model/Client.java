package ru.otus.core.model;

import ru.otus.core.Id;

import java.util.Random;

/**
 * @author sergey
 * created on 03.02.19.
 */
public class Client {
    transient private final static long DEFAULT_ID = 1;
    transient private final static String DEFAULT_NAME = "default_name";
    transient private final static int DEFAULT_AGE = 20;

    @Id
    private final long id;
    private final String name;
    private final int age;

    public Client() {
        this.id = DEFAULT_ID;
        this.name = DEFAULT_NAME;
        this.age = DEFAULT_AGE;
    }

    public Client(long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Client(String name, int age) {
        this.id = new Random().nextLong();
        this.name = name;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
