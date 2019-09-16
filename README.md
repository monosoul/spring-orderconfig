# Spring Orderconfig
A library that provides a convenient way of configuring decorators order in Spring.

[![Build Status](https://travis-ci.com/monosoul/spring-orderconfig.svg?branch=master)](https://travis-ci.com/monosoul/spring-orderconfig)
[![codecov](https://codecov.io/gh/monosoul/spring-orderconfig/branch/master/graph/badge.svg)](https://codecov.io/gh/monosoul/spring-orderconfig)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.monosoul/spring-orderconfig/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.monosoul/spring-orderconfig)
![license](https://img.shields.io/github/license/monosoul/spring-orderconfig.svg)

## Getting Started
To add the dependency to you maven build, simply add this to your `pom.xml`:
```xml
<dependency>
    <groupId>com.github.monosoul</groupId>
    <artifactId>spring-orderconfig</artifactId>
    <version>0.0.2</version>
</dependency>
```

Or with gradle, using Kotlin DSL:
```kotlin
dependencies {
    implementation("com.github.monosoul:spring-orderconfig:0.0.2")
}
```

**Please, be advised that this library doesn't have any transitive dependencies on Spring Framework or Spring Boot,
so you have to add the necessary Spring dependencies on your own.**

This is done to avoid classpath pollution with multiple versions of spring.

### Prerequisites
This library has been tested with Spring Boot version `2.1.4`. Should also work fine with Spring Framework versions
 `>=4.1.x`.

The library is compiled with Java 8 as target, so you'd also need at least Java 8 to use it.

### Usage example
An example of 2 implementations of the same interface:
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

## Release History
* 0.0.2
    * Use delomboked sources for sources.jar and javadoc.jar generation
* 0.0.1
    * Initial release
    
## License
The software is licensed under the [Apache-2.0 License](LICENSE).