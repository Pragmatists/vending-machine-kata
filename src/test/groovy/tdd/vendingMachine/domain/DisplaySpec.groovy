package tdd.vendingMachine.domain

import spock.lang.Specification
import tdd.vendingMachine.domain.display.Screen

/**
 * @author kdkz
 */
class DisplaySpec extends Specification {

    private Display display

    def setup() {
        display = new Display()
    }

    def "show should invoke show method on given screen implementation"() {
        given:
        def screen = Mock(Screen)

        when:
        display.show(screen)

        then:
        1 * screen.show()
    }

}
