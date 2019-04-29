package com.github.monosoul.spring.order.support.processor;

interface BeanDefinitionSpecification {

    Class<?> getClazz();

    Class<?> getDependentClass();

    boolean isPrimary();
}
