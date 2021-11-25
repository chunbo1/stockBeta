package com.object.factory;
import lombok.var;
//import org.springframework.beans.factory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;

import java.util.List;


public class ObjectFactoryImpl<T> implements ObjectFactory<T> {

    private final ApplicationContext applicationContext;

    public ObjectFactoryImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public T create(Class<T> domainClass, Object... args) {
        return applicationContext.getBean(domainClass, args);
    }

    @Override
    public T create(String objectName, Object... args) {
        return (T) applicationContext.getBean(objectName, args);
    }

    @Override
    public T create(Class<?> clazz, List<Class<?>> genericTypes, Object... args) {
        Class<?>[] classes = new Class<?>[genericTypes.size()];
        classes = genericTypes.toArray(classes);
        final ResolvableType resolvableType = ResolvableType.forClassWithGenerics(clazz, classes);
        final var beanProvider = applicationContext.getBeanProvider(resolvableType);
        final Object object = beanProvider.getObject(args);
        return (T) object;
    }
}
