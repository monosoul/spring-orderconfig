package com.github.monosoul.spring.order.support.processor;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;

@Value
@Builder
class BeanDefinitionSpecificationImpl implements BeanDefinitionSpecification {

    Class<?> clazz;
    @Default
    Class<?> dependentClass = Void.class;
    @Default
    boolean primary = false;
}
