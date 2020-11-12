package ru.otus;

import ru.otus.logger.Logger;

public class Demo {
    public static void main(String[] args) {
        System.out.println("----- TestLoggingImpl1 -----");
        TestLogging testLogging = Logger.createTestLogging(new TestLoggingImpl1());
        // Do log
        testLogging.calculation(6);
        testLogging.calculation(6, 7);
        testLogging.calculation(6, "Do log");
        // Do not log
        testLogging.doNotLogCalculation(1, 2);
        testLogging.calculation2("Don't log", 2);

        System.out.println("");
        System.out.println("----- TestLoggingImpl2 -----");
        testLogging = Logger.createTestLogging(new TestLoggingImpl2());
        // Do log
        testLogging.calculation(6);
        testLogging.calculation(6, "Do log");
        testLogging.calculation2("Do log", 2);
        // Do not log
        testLogging.calculation(6, 7);
        testLogging.doNotLogCalculation(1, 2);
    }
}