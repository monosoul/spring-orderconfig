package com.github.monosoul.spring.order.support.processor

import com.github.monosoul.ExtendedSpecification
import com.github.monosoul.SomeInterface
import com.github.monosoul.spring.order.OrderConfig
import com.github.monosoul.spring.order.OrderConfigItem
import org.springframework.beans.factory.support.AbstractBeanDefinition
import org.springframework.beans.factory.support.BeanDefinitionRegistry

import java.util.function.Function

class OrderConfigProcessorSpec extends ExtendedSpecification {

    def beanDefinitionRegistry = Mock(BeanDefinitionRegistry)
    def beanDefinition = Mock(AbstractBeanDefinition)
    def beanDefinitionProvider = Mock(Function) as Function<BeanDefinitionSpecification, AbstractBeanDefinition>
    def orderConfig = Mock(OrderConfig) {
        getItems() >> [
                Mock(OrderConfigItem) {
                    getClazz() >> SomeClass
                    getBeanName() >> "someBean"
                } as OrderConfigItem<SomeClass>,
                Mock(OrderConfigItem) {
                    getClazz() >> AnotherClass
                    getBeanName() >> "anotherBean"
                } as OrderConfigItem<AnotherClass>
        ]
    } as OrderConfig<SomeInterface>

    def processor = new OrderConfigProcessor(beanDefinitionRegistry, beanDefinitionProvider)

    def "should create and register bean definitions, first one should be primary"() {
        when:
            processor.accept(orderConfig)
        then:
            1 * beanDefinitionProvider.apply(_ as BeanDefinitionSpecification) >> { BeanDefinitionSpecification specification ->
                assert specification.primary
                assert specification.getClazz() == SomeClass
                assert specification.getDependentClass() == Void

                beanDefinition
            }
            1 * beanDefinitionRegistry.registerBeanDefinition("someBean", beanDefinition)
        and:
            1 * beanDefinitionProvider.apply(_ as BeanDefinitionSpecification) >> { BeanDefinitionSpecification specification ->
                assert !specification.primary
                assert specification.getClazz() == AnotherClass
                assert specification.getDependentClass() == SomeClass

                beanDefinition
            }
            1 * beanDefinitionRegistry.registerBeanDefinition("anotherBean", beanDefinition)
        and:
            0 * beanDefinitionProvider._
            0 * beanDefinitionRegistry._
    }

    private static class SomeClass implements SomeInterface {}

    private static class AnotherClass implements SomeInterface {}
}
