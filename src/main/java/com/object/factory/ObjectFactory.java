package com.object.factory;

import java.util.List;

public interface ObjectFactory<T> {
    T create(Class<T> domainClass, Object... args);
    T create(String objectName, Object... args);
    T create(Class<?> clazz, List<Class<?>> genericTypes, Object... args);
}
