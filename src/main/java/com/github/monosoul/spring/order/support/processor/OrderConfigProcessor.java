package com.github.monosoul.spring.order.support.processor;

import com.github.monosoul.spring.order.OrderConfig;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

@RequiredArgsConstructor
final class OrderConfigProcessor implements Consumer<OrderConfig<?>> {

    private final BeanDefinitionRegistry beanDefinitionRegistry;
    private final Function<BeanDefinitionSpecification, AbstractBeanDefinition> beanDefinitionProvider;

    @Override
    public void accept(final OrderConfig<?> orderConfig) {
        Class<?> dependentClass = Void.class;
        for (int i = 0; i < orderConfig.getItems().size(); i++) {
            val orderConfigItem = orderConfig.getItems().get(i);
            val currentBeanClass = orderConfigItem.getClazz();

            beanDefinitionRegistry.registerBeanDefinition(
                    orderConfigItem.getBeanName(),
                    beanDefinitionProvider.apply(
                            BeanDefinitionSpecificationImpl
                                    .builder()
                                    .clazz(currentBeanClass)
                                    .dependentClass(dependentClass)
                                    .primary(i == 0)
                                    .build()
                    )
            );

            dependentClass = currentBeanClass;
        }
    }
}
