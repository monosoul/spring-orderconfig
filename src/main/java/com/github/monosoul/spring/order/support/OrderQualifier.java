package com.github.monosoul.spring.order.support;

import com.github.monosoul.spring.order.OrderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Qualifier that is used internally by the {@link OrderConfig} processors.<br>
 * Points to the dependent class where containing class should be injected.<br>
 * Basically it's just reversed {@link Qualifier} and could be used like that.<br>
 * <br>
 * Example:
 * <pre>{@code
 * public interface SomeInterface {}
 *
 * @Component
 * public class SomeClass implements SomeInterface {
 *     private final SomeInterface dependency;
 *
 *     @Autowired
 *     public SomeClass(final SomeInterface dependency) {
 *         this.dependency = dependency;
 *     }
 * }
 *
 * @OrderQualifier(SomeClass.class)
 * @Component
 * public class AnotherClass implements SomeInterface {
 *     //some logic here
 * }
 *
 * }</pre>
 * Requires {@link OrderConfigConfiguration} to work.
 *
 * @see Qualifier
 * @see Component
 * @see Autowired
 * @see OrderConfig
 */
public @interface OrderQualifier {

	/**
	 * Dependent class
	 *
	 * @return a class instance
	 */
	@NonNull
	Class<?> value() default Void.class;
}
