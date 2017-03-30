package tdd.vendingMachine.listener

import spock.lang.Specification
import tdd.vendingMachine.domain.CashierPad
import tdd.vendingMachine.domain.Display
import tdd.vendingMachine.domain.display.impl.*

import static tdd.vendingMachine.domain.Denomination.FIVE
import static tdd.vendingMachine.domain.Denomination.TWO

/**
 * @author kdkz
 */
class VendingMachineListenerSpec extends Specification {

    private VendingMachineListener listener

    def setup() {
        listener = new VendingMachineListener()
    }

    def "clearContents should clear productPrice, currentInsertedAmount and return inserted coins"() {
        given:
        listener.cashierPad = Mock(CashierPad)
        and:
        listener.productPrice = new BigDecimal(5.5)
        and:
        listener.currentInsertedAmount = new BigDecimal(2.5)

        when:
        listener.clearContents()

        then:
        listener.productPrice == null
        listener.currentInsertedAmount == null
        1 * listener.cashierPad.returnInsertedCoins()
    }

    def "tryBuyProduct should return proper boolean value for given arguments"() {
        given:
        listener.currentInsertedAmount = insertedAmount
        and:
        listener.productPrice = productPrice
        and:
        listener.cashierPad = Mock(CashierPad)
        and:
        listener.display = Mock(Display)

        when:
        def result = listener.tryBuyProduct()

        then:
        result == ecpectedResult
        countRestMethodInvocation * listener.cashierPad.payAndReturnChange(*_)
        productSoldScreenInvocation * listener.display.show(*_) >> {
            arguments ->
                assert arguments[0] instanceof ProductSoldScreen
        }

        where:
        insertedAmount | productPrice | ecpectedResult | productSoldScreenInvocation | countRestMethodInvocation
        5.5            | 4.5          | true           | 1                           | 1
        4.5            | 5.5          | false          | 0                           | 0
    }


    def "onCancelButtonClicked should clear all contents and show CancelScreen"() {
        given:
        listener.display = Mock(Display)
        and:
        listener.cashierPad = Mock(CashierPad)
        and:
        listener.productPrice = new BigDecimal(5.5)
        and:
        listener.currentInsertedAmount = new BigDecimal(2.5)

        when:
        listener.onCancelButtonClicked()

        then:
        listener.productPrice == null
        listener.currentInsertedAmount == null
        1 * listener.cashierPad.returnInsertedCoins()
        1 * listener.display.show(*_) >> {
            arguments ->
                assert arguments[0] instanceof CancelScreen
        }
    }

    def "onShelfSelected should set product price and display should show SelectedShelfScreen when valid number is given"() {
        given:
        listener.display = Mock(Display)

        when:
        listener.onShelfSelected(shelfNumber)

        then:
        listener.productPrice == productPrice
        selectedShelfScreen * listener.display.show(*_) >> {
            arguments ->
                assert arguments[0] instanceof SelectedShelfScreen
        }

        where:
        shelfNumber | productPrice | selectedShelfScreen
        3           | 1.25         | 1
        -1          | null         | 0
        4           | null         | 0
    }

    def "onCoinInsertion should show SelectShelfFirstScreen when product price is not set"() {
        given:
        listener.display = Mock(Display)
        and:
        listener.productPrice = productPrice

        when:
        listener.onCoinInsertion(denomination, quantity)

        then:
        selectShelfFirstScreen * listener.display.show(*_) >> {
            arguments ->
                assert arguments[0] instanceof SelectShelfFirstScreen
        }

        insertedCoinsStatusScreen * listener.display.show(*_) >> {
            arguments ->
                assert arguments[0] instanceof InsertedCoinsStatusScreen
        }

        productSoldScreen * listener.display.show(*_) >> {
            arguments ->
                assert arguments[0] instanceof ProductSoldScreen
        }

        unableToCountRestScreen * listener.display.show(*_) >> {
            arguments ->
                assert arguments[0] instanceof UnableToCountRestScreen
        }

        where:
        productPrice | selectShelfFirstScreen | insertedCoinsStatusScreen | productSoldScreen | unableToCountRestScreen | denomination | quantity
        null         | 1                      | 0                         | 0                 | 0                       | FIVE         | 1
        4.5          | 0                      | 1                         | 0                 | 0                       | TWO          | 2
        2            | 0                      | 0                         | 1                 | 0                       | TWO          | 1
        4.5          | 0                      | 0                         | 0                 | 1                       | TWO          | 3
    }
}
