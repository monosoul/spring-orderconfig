package com.github.monosoul.spring.order;

import com.github.monosoul.spring.order.support.OrderConfigConfiguration;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Provides a way of configuration of the same-typed beans that are depending on each other.
 * Classes specified in {@link OrderConfig#getItems()} method would be injected in the specified order.
 * <p>
 * Example:
 * <br>
 * <pre><code>
 * public interface SomeInterface {}
 *
 * {@literal @}Component
 *  public class SomeClass implements SomeInterface {
 *      private final SomeInterface dependency;
 *
 *     {@literal @}Autowired
 *      public SomeClass(final SomeInterface dependency) {
 *          this.dependency = dependency;
 *      }
 *  }
 *
 * {@literal @}Component
 *  public class AnotherClass implements SomeInterface {
 *      //some logic here
 *  }
 *
 * {@literal @}Configuration
 *  public class JavaConfig {
 *
 *     {@literal @}Bean
 *     {@literal public OrderConfig<SomeInterface>} someInterfaceOrderConfig() {
 *         {@literal return new OrderConfigImpl<>}(Arrays.asList(
 *              OrderConfigItemImpl.of(SomeClass.class),
 *              OrderConfigItemImpl.of(AnotherClass.class)
 *          ));
 *      }
 *  }
 *
 * </code></pre>
 * Is equal to:
 * <pre><code>
 * public interface SomeInterface {}
 *
 * {@literal @}Component
 *  public class SomeClass implements SomeInterface {
 *      private final SomeInterface dependency;
 *
 *     {@literal @}Autowired
 *      public SomeClass(@Qualifier("someClass") final SomeInterface dependency) {
 *          this.dependency = dependency;
 *      }
 *  }
 *
 * {@literal @}Component
 *  public class AnotherClass implements SomeInterface {
 *      //some logic here
 *  }
 *
 * </code></pre>
 * Requires {@link OrderConfigConfiguration} to work.
 *
 * @param <T> any class
 * @see OrderConfigImpl
 * @see OrderConfigItem
 * @see OrderConfigItemImpl
 * @see Arrays#asList(Object[])
 * @see Component
 * @see Configuration
 * @see Autowired
 * @see Qualifier
 */
@FunctionalInterface
public interface OrderConfig<T> {

    /**
     * Returns a list of order config items containing bean names and classes that are implement T.
     *
     * @return List of {@link OrderConfigItem}'s
     */
    List<? extends OrderConfigItem<? extends T>> getItems();
}
