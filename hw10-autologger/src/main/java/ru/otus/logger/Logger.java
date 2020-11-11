package ru.otus.logger;

import ru.otus.TestLogging;
import ru.otus.TestLoggingImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class Logger {

    private Logger() {
    }

    public static TestLogging createTestLogging() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLoggingImpl());
        return (TestLogging) Proxy.newProxyInstance(Logger.class.getClassLoader(),
                new Class<?>[]{TestLogging.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final TestLogging testLogging;

        DemoInvocationHandler(TestLogging testLogging) {
            this.testLogging = testLogging;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // Find `method` in `TestLoggingImpl` for check annotation `@Log`
            Method[] foundMethods = Arrays.stream(
                    TestLoggingImpl.class.getDeclaredMethods()
            ).filter(testLoggingMethod ->
                    testLoggingMethod.getName().equals(method.getName()) &&
                            Arrays.equals(testLoggingMethod.getGenericParameterTypes(), method.getGenericParameterTypes())
            ).toArray(Method[]::new);

            if (foundMethods.length != 1) {
                // assumed unreachable
                throw new IllegalStateException("There are " + foundMethods.length + " methods" +
                        " with equal name and param types: " + Arrays.toString(foundMethods));
            }

            if (foundMethods[0].isAnnotationPresent(Log.class)) {
                System.out.println("@Log: invoking method " + method.getName() + " with params " + Arrays.toString(args));
            }
            return method.invoke(testLogging, args);
        }
    }
}
