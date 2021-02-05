package ru.otus.logger;

import ru.otus.TestLogging;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class Logger {

    private Logger() {
    }

    public static TestLogging createTestLogging(TestLogging proxiedObject) {
        InvocationHandler handler = new DemoInvocationHandler(proxiedObject);
        return (TestLogging) Proxy.newProxyInstance(Logger.class.getClassLoader(),
                new Class<?>[]{TestLogging.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final TestLogging testLogging;
        Method[] loggingMethods;

        DemoInvocationHandler(TestLogging testLogging) {
            this.testLogging = testLogging;
            this.loggingMethods = Arrays.stream(testLogging.getClass().getDeclaredMethods())
                    .filter(testLoggingMethod -> testLoggingMethod.isAnnotationPresent(Log.class))
                    .toArray(Method[]::new);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // Find `method` in `loggingMethods`
            int i;
            for (i = 0; i < this.loggingMethods.length; i++) {
                if (this.loggingMethods[i].getName().equals(method.getName()) &&
                        Arrays.equals(this.loggingMethods[i].getGenericParameterTypes(), method.getGenericParameterTypes())) {
                    break;
                }
            }
            if (i < this.loggingMethods.length) {
                System.out.println("@Log: invoking method " + method.getName() + " with params " + Arrays.toString(args));
            }
            return method.invoke(testLogging, args);
        }
    }
}
