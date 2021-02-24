package ru.otus.appcontainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {
    private static final Logger logger = LoggerFactory.getLogger(AppComponentsContainerImpl.class);

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        // You code here...
        Object configClassInstance = createConfigClassInstance(configClass);

        List<Method> beanGenerators = Arrays.asList(configClass.getDeclaredMethods());
        beanGenerators.sort(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order()));
        for (var beanGenerator : beanGenerators) {
            logger.info("create bean with {} ({})", beanGenerator.getName(), Arrays.toString(beanGenerator.getGenericParameterTypes()));
            try {
                Object bean = createBean(configClassInstance, beanGenerator);
                appComponents.add(bean);
                appComponentsByName.put(beanGenerator.getAnnotation(AppComponent.class).name(), bean);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        logger.info("appComponents: {}", appComponents);
        logger.info("appComponentsByName: {}", appComponentsByName);
    }

    private Object createBean(Object configClassInstance, Method generator) throws InvocationTargetException, IllegalAccessException {
        var params = generator.getParameters();
        return generator.invoke(
                configClassInstance,
                Arrays.stream(params)
                        .map(Parameter::getType)
                        .map(this::getAppComponent)
                        .toArray());
    }

    private Object createConfigClassInstance(Class<?> configClass) {
        try {
            return configClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        var foundAppComponents = appComponents.stream().filter(componentClass::isInstance).collect(Collectors.toList());
        if (foundAppComponents.size() != 1) {
            throw new IllegalArgumentException(String.format("Found %d appComponent for name %s", foundAppComponents.size(), componentClass.getName()));
        }
        return (C) foundAppComponents.get(0);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
