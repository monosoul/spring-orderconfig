package integration

import com.github.monosoul.spring.order.OrderConfig
import com.github.monosoul.spring.order.support.OrderConfigConfiguration
import integration.beans.CruelDecorator
import integration.beans.HelloDecorator
import integration.beans.MessageInterface
import integration.beans.WorldImplementation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import spock.lang.Specification

import static com.github.monosoul.spring.order.OrderConfigItemImpl.of

@SpringBootTest(classes = TestConfig)
class OrderConfigIntegrationSpec extends Specification {

    @Autowired
    MessageInterface helloCruelWorldService

    def "test integration"() {
        when:
            def actual = helloCruelWorldService.print()
        then:
            actual == "Hello cruel world!"
    }
}

@Configuration
@ComponentScan("integration.beans")
@Import(OrderConfigConfiguration)
class TestConfig {

    @Bean
    OrderConfig<MessageInterface> orderConfig() {
        return {
            [
                    of(CruelDecorator),
                    of(HelloDecorator),
                    of(WorldImplementation)
            ]
        }
    }
}
