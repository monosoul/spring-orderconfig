package com.github.monosoul.spring.order

import com.github.monosoul.ExtendedSpecification

class OrderConfigItemImplSpec extends ExtendedSpecification {

	def "constructor should throw an NPE when any of the arguments is null"() {
		when:
			new OrderConfigItemImpl<>(beanName, clazz)
		then:
			thrown(NullPointerException)
		where:
			beanName       | clazz
			null           | SomeClass.class
			randomString() | null
			null           | null
	}

	def "constructor should throw an NPE when the argument is null"() {
		when:
			new OrderConfigItemImpl<>(null)
		then:
			thrown(NullPointerException)
	}

	def "getBeanName() should return bean name if it's provided"() {
		setup:
			def item = new OrderConfigItemImpl<>(beanName, clazz)
		when:
			def actual = item.getBeanName()
		then:
			actual == beanName
		where:
			beanName << generate { randomString() }
			clazz = SomeClass.class
	}

	def "getBeanName() should return class name if no bean name provided"() {
		setup:
			def item = new OrderConfigItemImpl<>(clazz)
		when:
			def actual = item.getBeanName()
		then:
			actual == clazz.getName()
		where:
			clazz = SomeClass.class
	}

	def "getClazz() should return provided class"() {
		when:
			def actual = item.getClazz()
		then:
			actual == SomeClass.class
		where:
			item << [
					new OrderConfigItemImpl<>(SomeClass.class),
					new OrderConfigItemImpl<>(randomString(), SomeClass.class)
			]
	}

	def "of(beanName, clazz) should return a new item instance"() {
		when:
			def item = OrderConfigItemImpl.of(beanName, clazz)
		then:
			item.getBeanName() == beanName
			item.getClazz() == clazz
		where:
			beanName << generate { randomString() }
			clazz = SomeClass.class
	}

	def "of(clazz) should return a new item instance"() {
		when:
			def item = OrderConfigItemImpl.of(clazz)
		then:
			item.getBeanName() == clazz.getName()
			item.getClazz() == clazz
		where:
			clazz = SomeClass.class
	}

	private static class SomeClass {}
}
