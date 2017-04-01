package tdd.vendingMachine.domain

import spock.lang.Specification
import tdd.vendingMachine.domain.display.ScreenFactory
import tdd.vendingMachine.exception.MoneyChangeException
import tdd.vendingMachine.listener.VendingMachineObserver

import static tdd.vendingMachine.domain.Denomination.FIVE
import static tdd.vendingMachine.domain.display.ScreenFactory.ScreenType.CANCEL_SCREEN
import static tdd.vendingMachine.domain.display.ScreenFactory.ScreenType.INSERTED_COINS_STATUS_SCREEN
import static tdd.vendingMachine.domain.display.ScreenFactory.ScreenType.PRODUCT_SOLD_SCREEN
import static tdd.vendingMachine.domain.display.ScreenFactory.ScreenType.SELECTED_SHELF_SCREEN
import static tdd.vendingMachine.domain.display.ScreenFactory.ScreenType.SELECT_SHELF_FIRST_SCREEN
import static tdd.vendingMachine.domain.display.ScreenFactory.ScreenType.UNABLE_TO_COUNT_REST_SCREEN

/**
 * @author kdkz
 */
class VendingMachineSpec extends Specification {

    private VendingMachine vendingMachine

    private VendingMachineObserver observer

    private ScreenFactory screenFactory

    def setup() {
        vendingMachine = new VendingMachine()
        observer = Mock(VendingMachineObserver)
        vendingMachine.observers.add(observer)
        screenFactory = Mock(ScreenFactory)
        vendingMachine.screenFactory = screenFactory
    }

    def "clearContents should clear productPrice, currentInsertedAmount"() {
        given:
        vendingMachine.productPrice = new BigDecimal(5.5)
        and:
        vendingMachine.currentInsertedAmount = new BigDecimal(2.5)

        when:
        vendingMachine.clearContents()

        then:
        vendingMachine.productPrice == null
        vendingMachine.currentInsertedAmount == null
    }

    def "tryBuyProduct should return proper boolean value for given arguments"() {
        given:
        vendingMachine.currentInsertedAmount = insertedAmount
        and:
        vendingMachine.productPrice = productPrice

        when:
        def result = vendingMachine.tryBuyProduct()

        then:
        result == ecpectedResult
        sufficientValueInsertedInvocation * observer.sufficientAmountInserted(*_)

        where:
        insertedAmount | productPrice | ecpectedResult | sufficientValueInsertedInvocation
        4.5            | 4.5          | true           | 1
        4.5            | 5.5          | false          | 0
    }


    def "pressCancelButton should clear all contents and show CancelScreen"() {
        given:
        vendingMachine.productPrice = new BigDecimal(5.5)
        and:
        vendingMachine.currentInsertedAmount = new BigDecimal(2.5)

        when:
        vendingMachine.pressCancelButton()

        then:
        vendingMachine.productPrice == null
        vendingMachine.currentInsertedAmount == null
        1 * observer.cancelButtonSelected()
        1 * vendingMachine.screenFactory.displayScreen(*_) >> {
            arguments ->
                assert arguments[0] == CANCEL_SCREEN
        }
    }

    def "returnProductPrice should set product and display should show SelectedShelfScreen"() {
        given:
        def productPrice = 4.5

        when:
        vendingMachine.returnProductPrice(1, productPrice)

        then:
        vendingMachine.productPrice == productPrice
        1 * vendingMachine.screenFactory.displayScreen(*_) >> {
            arguments ->
                assert arguments[0] == SELECTED_SHELF_SCREEN
        }
    }

    def "selectShelf should invoke shelfSelected on observers for proper shelfNumber"() {
        when:
        vendingMachine.selectShelf(1)

        then:
        1 * observer.shelfSelected(*_)
    }

    def "insertCoins should show SelectShelfFirstScreen or invoke coinInserted on observers depending on product price is set"() {
        given:
        vendingMachine.productPrice = productPrice

        when:
        vendingMachine.insertCoins(FIVE, 1)

        then:
        coinInsertedInvocations * observer.coinInserted(*_)
        selectShelfFirstScreenInvocations * vendingMachine.screenFactory.displayScreen(*_) >> {
            arguments ->
                assert arguments[0] == SELECT_SHELF_FIRST_SCREEN
        }

        where:
        productPrice        | selectShelfFirstScreenInvocations | coinInsertedInvocations
        null                | 1                                 | 0
        new BigDecimal(5.5) | 0                                 | 1
    }

    def "isSufficient should check if already inserted amount is sufficient to buy a product"() {
        given:
        vendingMachine.productPrice = productPrice

        when:
        vendingMachine.isSufficient(alreadyInsertedAmount)

        then:
        sufficientAmountInsertedInvocation * observer.sufficientAmountInserted(*_)
        insertedCoinsStatusScreen * vendingMachine.screenFactory.displayScreen(*_) >> {
            arguments ->
                assert arguments[0] == INSERTED_COINS_STATUS_SCREEN
        }
        unableToCountRestScreen * vendingMachine.screenFactory.displayScreen(*_) >> {
            arguments ->
                assert arguments[0] == UNABLE_TO_COUNT_REST_SCREEN
        }

        where:
        productPrice | alreadyInsertedAmount | insertedCoinsStatusScreen | unableToCountRestScreen | sufficientAmountInsertedInvocation
        4.5          | 4                     | 1                         | 0                       | 0
        4.5          | 6                     | 0                         | 0                       | 1
        4.5          | 4.5                   | 0                         | 0                       | 1
    }
}
