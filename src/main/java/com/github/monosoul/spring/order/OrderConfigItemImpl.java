package com.github.monosoul.spring.order;

import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;

@EqualsAndHashCode
public final class OrderConfigItemImpl<T> implements OrderConfigItem<T> {

	private final String beanName;
	private final Class<T> clazz;

	public OrderConfigItemImpl(@lombok.NonNull final String beanName, @lombok.NonNull final Class<T> clazz) {
		this.beanName = beanName;
		this.clazz = clazz;
	}

	public OrderConfigItemImpl(@lombok.NonNull final Class<T> clazz) {
		this.beanName = null;
		this.clazz = clazz;
	}

	@NonNull
	@Override
	public String getBeanName() {
		return beanName == null ? clazz.getName() : beanName;
	}

	@NonNull
	@Override
	public Class<T> getClazz() {
		return clazz;
	}

	public static <T> OrderConfigItem<T> of(final String beanName, final Class<T> clazz) {
		return new OrderConfigItemImpl<>(beanName, clazz);
	}

	public static <T> OrderConfigItem<T> of(final Class<T> clazz) {
		return new OrderConfigItemImpl<>(clazz);
	}
}
