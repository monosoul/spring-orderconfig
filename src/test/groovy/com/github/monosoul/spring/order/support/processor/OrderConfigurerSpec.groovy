package com.github.monosoul.spring.order.support.processor

import com.github.monosoul.ExtendedSpecification
import com.github.monosoul.spring.order.OrderConfig
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionRegistry

import java.util.function.Consumer
import java.util.stream.Stream

class OrderConfigurerSpec extends ExtendedSpecification {

    def configurer = new OrderConfigurer()

    def "postProcessBeanFactory() should do nothing"() {
        setup:
            def beanFactory = Mock(ConfigurableListableBeanFactory)
        when:
            configurer.postProcessBeanFactory(beanFactory)
        then:
            0 * beanFactory._
    }

    def "postProcessBeanDefinitionRegistry() should call processors"() {
        setup:
            def orderConfigStream = Mock(Stream) as Stream<OrderConfig>
            def beansMap = Mock(Map) {
                values() >> Mock(List) {
                    stream() >> orderConfigStream
                }
            } as Map<String, OrderConfig>
            def beanFactory = Mock(FactoryAndRegistry) {
                getBeansOfType(OrderConfig.class) >> beansMap
            }
        when:
            configurer.postProcessBeanDefinitionRegistry(beanFactory as BeanDefinitionRegistry)
        then:
            1 * orderConfigStream.peek(_ as Consumer) >> { Consumer<OrderConfig<?>> consumer ->
                consumer instanceof OrderConfigPreProcessor

                orderConfigStream
            }
            1 * orderConfigStream.forEach(_ as Consumer) >> { Consumer<OrderConfig<?>> consumer ->
                consumer instanceof OrderConfigProcessor
            }
    }

    def "orderConfigPreProcessor() should return an instance of OrderConfigPreProcessor"() {
        setup:
            def registry = Mock(BeanDefinitionRegistry)
        when:
            def actual = configurer.orderConfigPreProcessor(registry)
        then:
            actual instanceof OrderConfigPreProcessor
            actual.beanDefinitionRegistry == registry
    }

    def "orderConfigProcessor() should return an instance of OrderConfigProcessor"() {
        setup:
            def registry = Mock(BeanDefinitionRegistry)
        when:
            def actual = configurer.orderConfigProcessor(registry)
        then:
            actual instanceof OrderConfigProcessor
            actual.beanDefinitionRegistry == registry
    }

    private static abstract class FactoryAndRegistry implements ListableBeanFactory, BeanDefinitionRegistry {}
}
