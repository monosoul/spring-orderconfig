package com.github.monosoul.spring.order.support;

import com.github.monosoul.spring.order.OrderConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration that enables {@link OrderConfig} support
 */
@Configuration
@ComponentScan("com.github.monosoul.spring.order.support")
public class OrderConfigConfiguration {

}
