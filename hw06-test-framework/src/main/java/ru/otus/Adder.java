package ru.otus;

public class Adder {

    public int sum2(int a, int b) {
        return a + b;
    }

    public int sumn(int... a) {
        int result = 0;
        for (int elem : a) {
            result += elem;
        }
        return result;
    }
}