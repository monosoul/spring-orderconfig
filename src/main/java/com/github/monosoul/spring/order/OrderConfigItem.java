package com.github.monosoul.spring.order;

import org.springframework.lang.NonNull;

/**
 * Provides a bean name and a bean class for the {@link OrderConfig}
 *
 * @param <T> any class
 */
public interface OrderConfigItem<T> {

    /**
     * Bean name
     *
     * @return bean name
     */
    @NonNull
    String getBeanName();

    /**
     * Bean class
     *
     * @return class
     */
    @NonNull
    Class<T> getClazz();
}
