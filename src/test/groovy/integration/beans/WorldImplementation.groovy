package integration.beans

import org.springframework.stereotype.Component

@Component
class WorldImplementation implements MessageInterface {

    @Override
    String print() {
        "world!"
    }
}
