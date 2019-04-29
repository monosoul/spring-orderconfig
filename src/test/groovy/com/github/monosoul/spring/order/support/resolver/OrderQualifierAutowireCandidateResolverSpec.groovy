package com.github.monosoul.spring.order.support.resolver

import com.github.monosoul.ExtendedSpecification
import com.github.monosoul.SomeInterface
import com.github.monosoul.spring.order.support.OrderQualifier
import org.springframework.beans.factory.config.BeanDefinitionHolder
import org.springframework.beans.factory.config.DependencyDescriptor
import org.springframework.beans.factory.support.AbstractBeanDefinition
import org.springframework.beans.factory.support.AutowireCandidateQualifier
import org.springframework.beans.factory.support.AutowireCandidateResolver
import spock.lang.Shared
import spock.lang.Unroll

import java.lang.reflect.Member

class OrderQualifierAutowireCandidateResolverSpec extends ExtendedSpecification {

    def internalResolver = Mock(AutowireCandidateResolver)
    def beanDefinition = Mock(AbstractBeanDefinition)
    def bdHolder = Mock(BeanDefinitionHolder) {
        getBeanDefinition() >> beanDefinition
    }
    @Shared
    def candidateQualifier = Mock(AutowireCandidateQualifier)
    def descriptor = Mock(DependencyDescriptor)

    def resolver = new OrderQualifierAutowireCandidateResolver(internalResolver)

    @Unroll
    def "isAutowireCandidate() should delegate to internal resolver when #condition"() {
        when:
            def actual = resolver.isAutowireCandidate(bdHolder, descriptor)
        then:
            _ * descriptor.getMember() >> Mock(Member.class) {
                getDeclaringClass() >> dependentType
            }
            _ * descriptor.getDependencyType() >> dependencyType
            _ * beanDefinition.getQualifier(OrderQualifier.class.getName()) >> qualififer
        and:
            1 * internalResolver.isAutowireCandidate(bdHolder, descriptor) >> internalResolverCallResult
            0 * internalResolver._
        expect:
            actual == internalResolverCallResult
        where:
            dependentType      | dependencyType  | qualififer         || condition
            NotAssignableClass | AssignableClass | candidateQualifier || "dependent type doesn't implements/extends dependency type"
            AssignableClass    | SomeInterface   | null               || "candidate bean definition doesn't have the qualifier"

            internalResolverCallResult = randomBoolean()
    }

    @Unroll
    def "isAutowireCandidate() should return #result if annotation value #condition"() {
        when:
            def actual = resolver.isAutowireCandidate(bdHolder, descriptor)
        then:
            1 * descriptor.getMember() >> Mock(Member.class) {
                getDeclaringClass() >> AssignableClass
            }
            1 * descriptor.getDependencyType() >> SomeInterface
            1 * bdHolder.getBeanDefinition() >> beanDefinition
            1 * beanDefinition.getQualifier(OrderQualifier.class.getName()) >> Mock(AutowireCandidateQualifier) {
                getAttribute("value") >> dependentType
            }
        and:
            0 * internalResolver._
        expect:
            actual == result
        where:
            dependentType      || result | condition
            NotAssignableClass || false  | "doesn't match dependent type"
            AssignableClass    || true   | "matches dependent type"
    }

    def "getSuggestedValue() should be delegated to the internal resolver"() {
        when:
            resolver.getSuggestedValue(descriptor)
        then:
            1 * internalResolver.getSuggestedValue(descriptor)
            0 * internalResolver._
    }

    def "getLazyResolutionProxyIfNecessary() should be delegated to the internal resolver"() {
        when:
            resolver.getLazyResolutionProxyIfNecessary(descriptor, beanName)
        then:
            1 * internalResolver.getLazyResolutionProxyIfNecessary(descriptor, beanName)
            0 * internalResolver._
        where:
            beanName << generate { randomString() }
    }

    private static class AssignableClass implements SomeInterface {}

    private static class NotAssignableClass {}
}
