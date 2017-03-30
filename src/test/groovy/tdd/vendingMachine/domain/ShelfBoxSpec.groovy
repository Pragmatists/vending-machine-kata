package tdd.vendingMachine.domain

import spock.lang.Specification
import tdd.vendingMachine.exception.ShelfNotExistException

/**
 * @author kdkz
 */
class ShelfBoxSpec extends Specification {

    private ShelfBox shelfBox

    def setup() {
        shelfBox = new ShelfBox()
    }

    def "selectShelfAndGetPrice should return proper price for valid shelf number"() {
        when:
        def price = shelfBox.selectShelfAndGetPrice(1)

        then:
        price == 1.5
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
}
