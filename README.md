# Spring Orderconfig
A library that provides a convenient way of configuring decorators order in Spring.

[![Build Status](https://travis-ci.com/monosoul/spring-orderconfig.svg?branch=master)](https://travis-ci.com/monosoul/spring-orderconfig)
[![codecov](https://codecov.io/gh/monosoul/spring-orderconfig/branch/master/graph/badge.svg)](https://codecov.io/gh/monosoul/spring-orderconfig)
![GitHub release](https://img.shields.io/github/release/monosoul/spring-orderconfig.svg)
![license](https://img.shields.io/github/license/monosoul/spring-orderconfig.svg)

### Usage example
A build script example:
```java
public interface SomeInterface {}

@Component
public class SomeClass implements SomeInterface {
    private final SomeInterface dependency;

    @Autowired
    public SomeClass(final SomeInterface dependency) {
        this.dependency = dependency;
    }
}

@Component
public class AnotherClass implements SomeInterface {
    //some logic here
}

@Configuration
public class JavaConfig {

    @Bean
    public OrderConfig<SomeInterface> someInterfaceOrderConfig() {
        return new OrderConfigImpl<>(Arrays.asList(
            OrderConfigItemImpl.of(SomeClass.class),
            OrderConfigItemImpl.of(AnotherClass.class)
        ));
    }
}
```

is equal to:

```java
public interface SomeInterface {}

@Component
public class SomeClass implements SomeInterface {
    private final SomeInterface dependency;

    @Autowired
    public SomeClass(@Qualifier("someClass") final SomeInterface dependency) {
        this.dependency = dependency;
    }
}

@Component
public class AnotherClass implements SomeInterface {
    //some logic here
}
```