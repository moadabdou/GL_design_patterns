package com.glproject.creational;

import com.glproject.domain.Element;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ElementFactory {

    private static final Map<Class<? extends Element>, ElementFactory> registry = new ConcurrentHashMap<>();

    static {
        for (ElementFactory factory : ServiceLoader.load(ElementFactory.class)) {
            registry.put(factory.getElementType(), factory);
        }
    }

    public abstract Element createElement(Map<String, String> properties);
    public abstract Class<? extends Element> getElementType();

    @SuppressWarnings("unchecked")
    public static <T extends Element> T createElement(Class<T> type, Map<String, String> properties) {
        ElementFactory factory = registry.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("No factory registered for " + type.getSimpleName()
                + ". Add a factory class to META-INF/services/" + ElementFactory.class.getName());
        }
        return (T) factory.createElement(properties);
    }
}
