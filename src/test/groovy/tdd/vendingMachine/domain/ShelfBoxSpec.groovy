package tdd.vendingMachine.domain

import spock.lang.Specification
import tdd.vendingMachine.exception.ShelfNotExistException
import tdd.vendingMachine.listener.VendingMachineNotifier

/**
 * @author kdkz
 */
class ShelfBoxSpec extends Specification {

    private ShelfBox shelfBox

    private VendingMachineNotifier notifier

    def setup() {
        notifier = Mock(VendingMachineNotifier)
        shelfBox = new ShelfBox(notifier)
    }

    def "selectShelfAndGetPrice should return proper price for valid shelf number"() {
        when:
        def price = shelfBox.selectShelfAndGetPrice(1)

        then:
        price == 7.4
    }

    def "selectShelfAndGetPrice should throw ShelfNotExistException when invalid number is given"() {
        when:
        shelfBox.selectShelfAndGetPrice(invalidNumber)

        then:
        thrown(ShelfNotExistException)

        where:
        invalidNumber | exceptionThrown
        -1            | true
        4             | true
    }

    def "shelfSelected should invoke notifyProductPrice on vendingMachineNotifier"() {
        when:
        shelfBox.shelfSelected(1)

        then:
        1 * notifier.notifyProductPrice(*_)
    }
}
