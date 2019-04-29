package com.github.monosoul.spring.order.support.resolver

import com.github.monosoul.ExtendedSpecification
import org.springframework.beans.factory.support.AutowireCandidateResolver
import org.springframework.beans.factory.support.DefaultListableBeanFactory

class OrderQualifierAutowireCandidateResolverConfigurerSpec extends ExtendedSpecification {

    def internalResolver = Mock(AutowireCandidateResolver)
    def beanFactory = Mock(DefaultListableBeanFactory)

    def configurer = new OrderQualifierAutowireCandidateResolverConfigurer()

    @SuppressWarnings("GroovyAccessibility")
    def "postProcessBeanFactory() should decorate factory's resolver with it's own resolver"() {
        when:
            configurer.postProcessBeanFactory(beanFactory)
        then:
            1 * beanFactory.getAutowireCandidateResolver() >> internalResolver
        and:
            1 * beanFactory.setAutowireCandidateResolver(_ as AutowireCandidateResolver) >> { AutowireCandidateResolver resolver ->
                assert resolver instanceof OrderQualifierAutowireCandidateResolver
                assert resolver.resolver == internalResolver
            }
    }
}
