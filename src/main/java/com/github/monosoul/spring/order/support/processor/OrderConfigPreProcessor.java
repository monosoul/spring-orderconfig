package com.github.monosoul.spring.order.support.processor;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;
import com.github.monosoul.spring.order.OrderConfig;
import com.github.monosoul.spring.order.OrderConfigItem;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

@RequiredArgsConstructor
final class OrderConfigPreProcessor implements Consumer<OrderConfig<?>> {

    private final BeanDefinitionRegistry beanDefinitionRegistry;

    @Override
    public void accept(final OrderConfig<?> orderConfig) {
        val classesSet = orderConfig.getItems().stream().map(OrderConfigItem::getClazz).map(Class::getName).collect(toSet());

        stream(beanDefinitionRegistry.getBeanDefinitionNames())
                .map(n -> new SimpleImmutableEntry<>(n, beanDefinitionRegistry.getBeanDefinition(n)))
                .filter(e -> classesSet.contains(e.getValue().getBeanClassName()))
                .forEach(e -> beanDefinitionRegistry.removeBeanDefinition(e.getKey()));
    }
}
