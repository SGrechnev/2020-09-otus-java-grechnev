package ru.otus;

import ru.otus.logger.Log;
import ru.otus.logger.SomeAnotherAnnotation;

public class TestLoggingImpl1 implements TestLogging {
    @Log
    public void calculation(int param) {
        System.out.println("\tImpl1.calculation(Integer)");
    }

    @Log
    public void calculation(int param1, int param2) {
        System.out.println("\tImpl1.calculation(Integer, Integer)");
    }

    @Log
    public void calculation(int param1, String param3) {
        System.out.println("\tImpl1.calculation(Integer, Integer, String)");
    }

    public void doNotLogCalculation(int param1, int param2) {
        System.out.println("\tImpl1.doNotLogCalculation(Integer, Integer)");
    }

    @SomeAnotherAnnotation
    public void calculation2(String param1, int param2) {
        System.out.println("\tImpl1.calculation2(String, Integer)");
    }

}