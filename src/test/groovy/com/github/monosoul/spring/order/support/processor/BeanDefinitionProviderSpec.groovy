package com.github.monosoul.spring.order.support.processor

import com.github.monosoul.ExtendedSpecification
import com.github.monosoul.spring.order.support.OrderQualifier

import static org.springframework.beans.factory.support.AbstractBeanDefinition.AUTOWIRE_AUTODETECT

class BeanDefinitionProviderSpec extends ExtendedSpecification {

    def provider = new BeanDefinitionProvider()

    def "should throw exception if clazz is null"() {
        when:
            provider.apply(specification)
        then:
            def exception = thrown(IllegalArgumentException)
        expect:
            exception.getMessage() == "Clazz can't be null"
        where:
            specification = Mock(BeanDefinitionSpecification) {
                getClazz() >> null
            }
    }

    def "should throw exception if dependent class is null"() {
        when:
            provider.apply(specification)
        then:
            def exception = thrown(IllegalArgumentException)
        expect:
            exception.getMessage() == "Dependent class can't be null"
        where:
            specification = Mock(BeanDefinitionSpecification) {
                getClazz() >> SomeClass
                getDependentClass() >> null
            }
    }

    def "should provide bean definition"() {
        when:
            def actual = provider.apply(specification)
        then:
            actual.isPrimary() == specification.isPrimary()
            //noinspection GrDeprecatedAPIUsage
            actual.autowireMode == AUTOWIRE_AUTODETECT
            actual.getBeanClass() == specification.getClazz()
        and:
            def candidateQualifier = actual.getQualifier(OrderQualifier.getName());
            candidateQualifier != null
            candidateQualifier.getAttribute("value") == specification.getDependentClass()
        where:
            specification << generate {
                Mock(BeanDefinitionSpecification) {
                    isPrimary() >> randomBoolean()
                    getClazz() >> SomeClass
                    getDependentClass() >> AnotherClass
                }
            }
    }

    private static class SomeClass {}

    private static class AnotherClass {}
}
