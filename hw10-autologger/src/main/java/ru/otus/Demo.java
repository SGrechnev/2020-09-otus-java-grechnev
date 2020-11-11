package ru.otus;

import ru.otus.logger.Logger;

public class Demo {
    public static void main(String[] args) {
        TestLogging testLogging = Logger.createTestLogging();
        // Do log
        testLogging.calculation(6);
        testLogging.calculation(6, 7);
        testLogging.calculation(6, "some string");
        // Do not log
        testLogging.doNotLogCalculation(1, 2);
        testLogging.doNotLogCalculation2(1, 2);
    }
}