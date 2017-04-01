package tdd.vendingMachine.domain

import spock.lang.Specification
import tdd.vendingMachine.exception.ShelfNotExistException
import tdd.vendingMachine.listener.VendingMachineNotifier

import static tdd.vendingMachine.domain.Product.CHOCOLATE_BAR
import static tdd.vendingMachine.domain.Product.COLA
import static tdd.vendingMachine.domain.Product.MINERAL_WATER

/**
 * @author kdkz
 */
class ShelfBoxSpec extends Specification {

    private ShelfBox shelfBox

    private VendingMachineNotifier notifier

    def setup() {
        notifier = Mock(VendingMachineNotifier)
        shelfBox = new ShelfBox(notifier)
        shelfBox.addShelfToShelfBox(new Shelf(COLA));
        shelfBox.addShelfToShelfBox(new Shelf(CHOCOLATE_BAR));
        shelfBox.addShelfToShelfBox(new Shelf(MINERAL_WATER));
    }

    def "selectShelfAndGetPrice should return proper price for valid shelf number"() {
        given:
        shelfBox.selectedShelf = 0

        when:
        def price = shelfBox.selectShelfAndGetPrice(1)

        then:
        price == 7.4
        shelfBox.selectedShelf  == 1
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

    def "deselectShelf should deselect already selected shelf"() {
        given:
        shelfBox.selectedShelf = 1

        when:
        shelfBox.deselectShelf()

        then:
        shelfBox.selectedShelf  == 0
    }

    def "cancelButtonSelected should deselect already selected shelf"() {
        given:
        shelfBox.selectedShelf = 1

        when:
        shelfBox.cancelButtonSelected()

        then:
        shelfBox.selectedShelf  == 0
    }

    def "sufficientAmountInserted should invoke takeOffProduct method on Shelf object and deceledt shelf"() {
        given:
        shelfBox.selectedShelf = 1
        and:
        def shelf = Mock(Shelf)
        and:
        shelfBox.shelves = [shelf]

        when:
        shelfBox.sufficientAmountInserted(BigDecimal.ONE)

        then:
        1 * shelf.takeOffProduct()
        shelfBox.selectedShelf == 0
    }
}
