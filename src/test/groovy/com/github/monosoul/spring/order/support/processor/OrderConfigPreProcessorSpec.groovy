package com.github.monosoul.spring.order.support.processor

import com.github.monosoul.ExtendedSpecification
import com.github.monosoul.SomeInterface
import com.github.monosoul.spring.order.OrderConfig
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import spock.lang.Unroll

import java.util.function.Function
import java.util.stream.Collector
import java.util.stream.Stream

class OrderConfigPreProcessorSpec extends ExtendedSpecification {

    def beanDefinitionRegistry = Mock(BeanDefinitionRegistry)
    def classNameSet = Mock(Set) as Set<String>
    def orderConfig = Mock(OrderConfig) {
        getItems() >> Mock(List) {
            stream() >> Mock(Stream) {
                map(_ as Function) >> it
                collect(_ as Collector) >> classNameSet
            }
        }
    } as OrderConfig<SomeInterface>
    def beanDefinition = Mock(BeanDefinition) {
        getBeanClassName() >> { randomString() }
    }

    def preProcessor = new OrderConfigPreProcessor(beanDefinitionRegistry);

    @Unroll
    def "should #action if #condition"() {
        when:
            preProcessor.accept(orderConfig)
        then:
            1 * beanDefinitionRegistry.getBeanDefinitionNames() >> beanDefinitionNames
            beanDefinitionNames.size() * beanDefinitionRegistry.getBeanDefinition(_ as String) >> beanDefinition
            beanDefinitionNames.size() * classNameSet.contains(_ as String) >> {
                expectedRemovals != 0
            }
        and:
            expectedRemovals * beanDefinitionRegistry.removeBeanDefinition(_ as String) >> { String beanName ->
                assert beanName in beanDefinitionNames
            }
        where:
            expectedRemovals | action               | condition
            0                | "do nothing"         | "order config items doesn't contain a class of such bean"
            LIMIT            | "remove definitions" | "config items contains classes of such beans"

            beanDefinitionNames = generate { randomString() } as String[]
    }
}
