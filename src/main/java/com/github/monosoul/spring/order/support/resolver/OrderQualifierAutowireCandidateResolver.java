package com.github.monosoul.spring.order.support.resolver;

import com.github.monosoul.spring.order.support.OrderQualifier;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.AutowireCandidateResolver;

@RequiredArgsConstructor
final class OrderQualifierAutowireCandidateResolver implements AutowireCandidateResolver {

    private final AutowireCandidateResolver resolver;

    @Override
    public boolean isAutowireCandidate(final BeanDefinitionHolder bdHolder, final DependencyDescriptor descriptor) {
        val dependentType = descriptor.getMember().getDeclaringClass();
        val dependencyType = descriptor.getDependencyType();
        val candidateBeanDefinition = (AbstractBeanDefinition) bdHolder.getBeanDefinition();

        if (dependencyType.isAssignableFrom(dependentType)) {
            val candidateQualifier = candidateBeanDefinition.getQualifier(OrderQualifier.class.getName());
            if (candidateQualifier != null) {
                return dependentType.equals(candidateQualifier.getAttribute("value"));
            }
        }

        return resolver.isAutowireCandidate(bdHolder, descriptor);
    }

    @Override
    public boolean isRequired(final DependencyDescriptor descriptor) {
        return resolver.isRequired(descriptor);
    }

    @Override
    public Object getSuggestedValue(final DependencyDescriptor descriptor) {
        return resolver.getSuggestedValue(descriptor);
    }

    @Override
    public Object getLazyResolutionProxyIfNecessary(final DependencyDescriptor descriptor, final String beanName) {
        return resolver.getLazyResolutionProxyIfNecessary(descriptor, beanName);
    }
}
