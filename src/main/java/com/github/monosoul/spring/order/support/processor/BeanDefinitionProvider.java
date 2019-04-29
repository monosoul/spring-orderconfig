package com.github.monosoul.spring.order.support.processor;

import static org.springframework.beans.factory.support.AbstractBeanDefinition.AUTOWIRE_AUTODETECT;
import static org.springframework.beans.factory.support.BeanDefinitionBuilder.genericBeanDefinition;
import com.github.monosoul.spring.order.support.OrderQualifier;
import java.util.function.Function;
import lombok.val;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.AutowireCandidateQualifier;
import org.springframework.util.Assert;

final class BeanDefinitionProvider implements Function<BeanDefinitionSpecification, AbstractBeanDefinition> {

    @Override
    public AbstractBeanDefinition apply(final BeanDefinitionSpecification specification) {
        Assert.notNull(specification.getClazz(), "Clazz can't be null");
        Assert.notNull(specification.getDependentClass(), "Dependent class can't be null");

        @SuppressWarnings("deprecation")
        val beanDefinition = genericBeanDefinition(specification.getClazz())
                .setAutowireMode(AUTOWIRE_AUTODETECT)
                .getBeanDefinition();
        beanDefinition.setPrimary(specification.isPrimary());
        beanDefinition.addQualifier(new AutowireCandidateQualifier(OrderQualifier.class, specification.getDependentClass()));

        return beanDefinition;
    }
}
