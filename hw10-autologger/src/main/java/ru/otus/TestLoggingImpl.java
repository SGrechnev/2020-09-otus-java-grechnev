package ru.otus;

import ru.otus.logger.Log;
import ru.otus.logger.SomeAnotherAnnotation;

public class TestLoggingImpl implements TestLogging {
    @Log
    public void calculation(int param) {
//        System.out.println("\tTestLogging.calculation(Integer)");
    }

    @Log
    public void calculation(int param1, int param2) {
//        System.out.println("\tTestLogging.calculation(Integer, Integer)");
    }

    @Log
    public void calculation(int param1, String param3) {
//        System.out.println("\tTestLogging.calculation(Integer, Integer, String)");
    }

    public void doNotLogCalculation(int param1, int param2) {
//        System.out.println("\tTestLogging.doNotLogCalculation(Integer, Integer)");
    }

    @SomeAnotherAnnotation
    public void doNotLogCalculation2(int param1, int param2) {
//        System.out.println("\tTestLogging.doNotLogCalculation2(Integer, Integer)");
    }

}