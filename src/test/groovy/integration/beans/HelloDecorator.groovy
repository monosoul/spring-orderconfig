package integration.beans

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class HelloDecorator implements MessageInterface {

    private final MessageInterface worldService

    @Autowired
    HelloDecorator(final MessageInterface worldService) {
        this.worldService = worldService
    }

    @Override
    String print() {
        return "Hello " + worldService.print()
    }
}
