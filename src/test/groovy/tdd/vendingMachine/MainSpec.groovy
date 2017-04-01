package tdd.vendingMachine

import spock.lang.Specification
import tdd.vendingMachine.domain.CashierPad
import tdd.vendingMachine.domain.Shelf
import tdd.vendingMachine.domain.ShelfBox
import tdd.vendingMachine.domain.VendingMachine
import tdd.vendingMachine.domain.display.ScreenFactory
import tdd.vendingMachine.domain.strategy.impl.HighestFirstMoneyChangeStrategy
import tdd.vendingMachine.listener.VendingMachineNotifier

import static tdd.vendingMachine.domain.Denomination.*
import static tdd.vendingMachine.domain.Product.CHOCOLATE_BAR
import static tdd.vendingMachine.domain.Product.COLA
import static tdd.vendingMachine.domain.Product.MINERAL_WATER

/**
 * @author kdkz
 */
class MainSpec extends Specification {

    private VendingMachine vendingMachine

    private ScreenFactory screenFactory

    private CashierPad cashierPad

    private ShelfBox shelfBox

    def setup() {
        powerUp()
    }

    /**
     * Util method for initializing Vending Machine
     */
    def powerUp() {
        vendingMachine = Spy(VendingMachine)
        VendingMachineNotifier vendingMachineNotifier = new VendingMachineNotifier();
        vendingMachineNotifier.setVendingMachine(vendingMachine);

        screenFactory = Spy(ScreenFactory);
        vendingMachine.adDisplayMechanism(screenFactory)

        HighestFirstMoneyChangeStrategy changeStrategy = Spy(HighestFirstMoneyChangeStrategy)

        cashierPad = Spy(CashierPad, constructorArgs: [changeStrategy, vendingMachineNotifier])
        shelfBox = Spy(ShelfBox, constructorArgs: [vendingMachineNotifier]);
        shelfBox.addShelfToShelfBox(new Shelf(COLA));
        shelfBox.addShelfToShelfBox(new Shelf(CHOCOLATE_BAR));
        shelfBox.addShelfToShelfBox(new Shelf(MINERAL_WATER));

        vendingMachine.registerObserver(cashierPad);
        vendingMachine.registerObserver(shelfBox);
    }

    def "insert coin without selecting shelf"() {
        expect:
        vendingMachine.insertCoins(FIVE, 1)
    }

    def "select shelf nr 3"() {
        given:
        int shelfNumber = 3

        expect:
        vendingMachine.selectShelf(shelfNumber)
    }

    def "select cancel button"() {
        expect:
        vendingMachine.pressCancelButton()
    }

    def "select shelf nr 2 and insert 0.20 x 3 at once and then cancel. Vending machine has no coins"(){
        given:
        int shelfNumber = 2

        expect:
        vendingMachine.selectShelf(shelfNumber)
        vendingMachine.insertCoins(ONE_FIFTH, 1)
        vendingMachine.insertCoins(ONE_FIFTH, 1)
        vendingMachine.insertCoins(ONE_FIFTH, 1)
        vendingMachine.pressCancelButton()
    }

    def "select shelf nr 1 and insert exact amount as product price. Vending machine has no coins"() {
        given:
        int shelfNumber = 1

        expect:
        vendingMachine.selectShelf(shelfNumber)
        vendingMachine.insertCoins(FIVE, 1)
        vendingMachine.insertCoins(TWO, 1)
        vendingMachine.insertCoins(ONE_FIFTH, 1)
        vendingMachine.insertCoins(ONE_TENTH, 1)
        vendingMachine.insertCoins(ONE_TENTH, 1)
    }

    def "select shelf nr 2 and insert more amount than product price. Vending machine has no coins. Rest will be returned."(){
        given:
        int shelfNumber = 2

        expect:"Vending Machine does not have enough coins to count the rest so transaction will be canceled and inserted coins will be returned"
        vendingMachine.selectShelf(shelfNumber)
        vendingMachine.insertCoins(ONE_FIFTH, 1)
        vendingMachine.insertCoins(ONE_FIFTH, 1)
        vendingMachine.insertCoins(ONE_FIFTH, 1)
        vendingMachine.insertCoins(HALF, 1)
        vendingMachine.insertCoins(ONE_FIFTH, 1)
    }

    def "select shelf nr 2 and insert more amount than product price. Vending machine has no coins. Transaction will finish properly."(){
        given:
        int shelfNumber = 2

        expect:"Vending Machine is empty on the beginning but is able to count the rest from already inserted coins."
        vendingMachine.selectShelf(shelfNumber)
        vendingMachine.insertCoins(ONE_FIFTH, 1)
        vendingMachine.insertCoins(ONE_FIFTH, 1)
        vendingMachine.insertCoins(ONE_FIFTH, 1)
        vendingMachine.insertCoins(ONE_FIFTH, 1)
        vendingMachine.insertCoins(ONE_TENTH, 1)
        vendingMachine.insertCoins(ONE_FIFTH, 1)
        vendingMachine.insertCoins(ONE_FIFTH, 1)
    }

    def "try to buy 2 products from shelf nr 3 one by one"() {
        given:
        int shelfNumber1 = 3
        int shelfNumber2 = 2

        expect:"Product 2 will not be sold because it is not possible to count the rest."
        vendingMachine.selectShelf(shelfNumber1)
        vendingMachine.insertCoins(TWO, 1)
        vendingMachine.insertCoins(HALF, 1)
        vendingMachine.insertCoins(HALF, 1)
        vendingMachine.insertCoins(HALF, 1)
        vendingMachine.insertCoins(ONE_FIFTH, 1)

        vendingMachine.selectShelf(shelfNumber2)
        vendingMachine.insertCoins(FIVE, 1)
    }
}
