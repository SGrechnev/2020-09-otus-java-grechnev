package ru.otus;

import ru.otus.testframework.After;
import ru.otus.testframework.Before;
import ru.otus.testframework.Test;

public class AdderTest {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";

    public static final String TEST_STRING = ANSI_GREEN + "@Test" + ANSI_RESET;
    public static final String BEFORE_STRING = ANSI_YELLOW + "@Before" + ANSI_RESET;
    public static final String AFTER_STRING = ANSI_BLUE + "@After" + ANSI_RESET;

    Adder adder;

    public AdderTest(){
        System.out.println(ANSI_RED + "AdderTest" + ANSI_RESET + " constructor");
    }

    @Before
    public void setAdder() {
        this.adder = new Adder();
        System.out.println(BEFORE_STRING + ": setAdder");
    }

    @Before
    public void nopBefore() {
        System.out.println(BEFORE_STRING + ": nopBefore");
    }

    @Test
    public void sum2Success() {
        System.out.println(TEST_STRING + ": sum2Success");
        int a = 42, b = 24;
        if (adder.sum2(a, b) != a + b) {
            throw new AssertionError("sum2(42, 24) != 42 + 24");
        }
    }

    @Test
    public void sum2Fail() {
        System.out.println(TEST_STRING + ": sum2Fail start");
        if (adder.sum2(42, 24) != 42) {
            throw new AssertionError("sum2(42, 24) != 42");
        }
        System.out.println(TEST_STRING + ": sum2Fail end");
    }

    @Test
    public void sumnShouldReturnSumAll() {
        System.out.println(TEST_STRING + ": sumnShouldReturnSumAll");
        if (adder.sumn(1, 2, 3, 4, 5) != 1 + 2 + 3 + 4 + 5) {
            throw new AssertionError("sumn(1, 2, 3, 4, 5) != 15)");
        }
    }

    @Test
    public void sumnFail() {
        System.out.println(TEST_STRING + ": sumnFail start");
        if (adder.sumn(42, 24) != 42) {
            throw new ArithmeticException("sumn(42, 24) != 42");
        }
        System.out.println(TEST_STRING + ": sumnFail end");
    }

    @After
    public void cleanup() {
        System.out.println(AFTER_STRING + ": cleanup");
    }

    @After
    public void nopAfter() {
        System.out.println(AFTER_STRING + ": nopAfter");
    }
}