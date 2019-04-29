package com.github.monosoul

import org.apache.commons.lang3.RandomStringUtils
import spock.lang.Specification

abstract class ExtendedSpecification extends Specification {

	static final LIMIT = 10

	def randomString() {
		RandomStringUtils.randomAlphabetic(LIMIT)
	}

	def <T> List<T> generate(final Closure<T> generate) {
		(0..LIMIT).collect(generate)
	}
}
