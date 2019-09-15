package integration.beans

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CruelDecorator implements MessageInterface {

    private final MessageInterface helloWorldService

    @Autowired
    CruelDecorator(final MessageInterface helloWorldService) {
        this.helloWorldService = helloWorldService
    }

    @Override
    String print() {
        def message =  helloWorldService.print().split(" ")
        message[0] + " cruel " + message[1]
    }
}
