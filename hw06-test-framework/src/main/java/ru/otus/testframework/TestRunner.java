package ru.otus.testframework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Запускает тесты (отмеченные @Test методы) переданного класса
 */
public class TestRunner {

    Class<?> testClass = null;
    private Map<String, TestResult> results;

    public TestRunner(String className) {
        try {
            this.testClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.out.println("[ERROR] Class " + e.getMessage() + " is not found");
        }
    }

    public static void main(String[] args) {
        TestRunner testRunner = new TestRunner("example.class.name");
        testRunner.run();
        testRunner = new TestRunner("ru.otus.AdderTest");
        testRunner.run();
        System.out.println("\nResults:");
        testRunner.printSummary();
    }

    public void run() {
        // Clean results before current run
        results = new HashMap<>();

        if (testClass == null) {
            System.out.println("[ERROR] testClass is not set");
            return;
        }

        Method[] methods;
        try {
            methods = testClass.getMethods();
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
            return;
        }

        Method[] allBefore = Arrays.stream(methods).filter(method -> method.isAnnotationPresent(Before.class)).toArray(Method[]::new);
        Method[] allAfter = Arrays.stream(methods).filter(method -> method.isAnnotationPresent(After.class)).toArray(Method[]::new);
        Stream<Method> allTest = Arrays.stream(methods).filter(method -> method.isAnnotationPresent(Test.class));

        allTest.forEach(test -> {
            runTest(allBefore, test, allAfter);
        });
    }

    private void runTest(Method[] allBefore, Method test, Method[] allAfter) {
        Object testInstance;
        try {
            testInstance = testClass.getConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            results.put(test.getName(), new TestResult(false, e));
            return;
        }

        // Call all @Before methods
        Arrays.stream(allBefore).forEach(method -> safeInvoke(testInstance, method));

        try {
            test.invoke(testInstance);
            results.put(test.getName(), new TestResult(true));
        } catch (IllegalAccessException | InvocationTargetException | AssertionError e) {
            results.put(test.getName(), new TestResult(false, e));
        }

        // Call all @Before methods
        Arrays.stream(allAfter).forEach(method -> safeInvoke(testInstance, method));
    }

    private void safeInvoke(Object testInstance, Method method) {
        try {
            method.invoke(testInstance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public void printSummary(){
        long numberOfSuccess = results.entrySet().stream().filter( entry -> entry.getValue().isSuccess() ).count();
        System.out.println(results.size() + " tests completed, " + numberOfSuccess + " passed, " + (results.size() - numberOfSuccess) + " failed");
        System.out.println("Fails:");
        results.entrySet().stream().filter( entry -> entry.getValue().isFail() ).forEach( entry -> System.out.println(entry) );
    }
}