package ru.otus.core.model;

import ru.otus.core.Id;

import java.util.Random;

public class Account {
    transient private final static String DEFAULT_NO = "2200123456781234";
    transient private final static String DEFAULT_TYPE = "default_type";
    transient private final static float DEFAULT_REST = 4.2f;

    @Id
    private final String no;
    private final String type;
    private final float rest;

    public Account() {
        this.no = DEFAULT_NO;
        this.type = DEFAULT_TYPE;
        this.rest = DEFAULT_REST;
    }

    public Account(String no, String type, float rest) {
        this.no = no;
        this.type = type;
        this.rest = rest;
    }

    public Account(String type, float rest) {
        this.no = Long.toString(new Random().nextLong());
        this.type = type;
        this.rest = rest;
    }

    public String getNo() {
        return no;
    }

    public String getType() {
        return type;
    }

    public float getRest() {
        return rest;
    }

    @Override
    public String toString() {
        return "Account{" +
                "no='" + no + '\'' +
                ", type='" + type + '\'' +
                ", rest=" + rest +
                '}';
    }
}
