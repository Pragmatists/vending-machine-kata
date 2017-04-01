package tdd.vendingMachine.domain

import spock.lang.Specification
import tdd.vendingMachine.domain.strategy.MoneyChangeStrategy
import tdd.vendingMachine.domain.strategy.impl.HighestFirstMoneyChangeStrategy
import tdd.vendingMachine.exception.MoneyChangeException
import tdd.vendingMachine.listener.VendingMachineNotifier

import static tdd.vendingMachine.domain.Denomination.*

/**
 * @author kdkz
 */
class CashierPadSpec extends Specification {

    private CashierPad cashierPad

    private VendingMachineNotifier vendingMaichineNotifier

    def setup() {
        def moneyChangeStrategy = new HighestFirstMoneyChangeStrategy()
        vendingMaichineNotifier = Mock(VendingMachineNotifier)
        cashierPad = new CashierPad(moneyChangeStrategy, vendingMaichineNotifier)
    }

    def "getAmountFromCoins returns amount from given coins quantity map"() {
        expect:
        expectedAmount == cashierPad.getAmountFromCoins(coinsQuantity)

        where:
        expectedAmount | coinsQuantity
        43.4           | [(FIVE): 6, (TWO): 3, (HALF): 5, (ONE): 3, (ONE_FIFTH): 5, (ONE_TENTH): 9]
        42.4           | [(FIVE): 6, (TWO): 3, (HALF): 5, (ONE): 3, (ONE_TENTH): 9]
    }

    def "subtractCoinsInCashierAndRestCoins should return subtraction of coins in cashier pad and given rest coins"() {
        when:
        def subtractedCoinsQuantity = cashierPad.subtractCoinsInCashierAndRestCoins(coinsToRemove, coinsQuantity)

        then:
        subtractedCoinsQuantity == expectedCoins

        where:
        coinsToRemove                                    | coinsQuantity                                                              | expectedCoins
        [(FIVE): 2, (HALF): 4, (ONE): 1, (ONE_TENTH): 9] | [(FIVE): 6, (TWO): 3, (HALF): 5, (ONE): 3, (ONE_FIFTH): 5, (ONE_TENTH): 9] | [(FIVE): 4, (TWO): 3, (HALF): 1, (ONE): 2, (ONE_FIFTH): 5, (ONE_TENTH): 0]
    }

    def "sumInsertedCoinsWithCoinsInCashier should return subtraction of coins in cashier pad and given rest coins"() {
        given:
        cashierPad.coinsInCashier = coinsQuantity
        and:
        cashierPad.insertedCoins = insertedCoins

        when:
        def summedCoinsQuantity = cashierPad.sumInsertedCoinsWithCoinsInCashier()

        then:
        summedCoinsQuantity == expectedCoins

        where:
        insertedCoins                    | coinsQuantity                                                    | expectedCoins
        [(FIVE): 2, (HALF): 4, (ONE): 1] | [(FIVE): 6, (TWO): 3, (HALF): 5, (ONE_FIFTH): 5, (ONE_TENTH): 9] | [(FIVE): 8, (TWO): 3, (HALF): 9, (ONE): 1, (ONE_FIFTH): 5, (ONE_TENTH): 9]
    }

    def "changeCashierState should change state of cashier pad by given inserted coins quantity and rest coins quantity"() {
        given:
        cashierPad.coinsInCashier = [(FIVE): 6, (TWO): 3, (HALF): 5, (ONE_FIFTH): 5, (ONE_TENTH): 9]
        cashierPad.insertedCoins = insertedCoins

        when:
        cashierPad.changeCashierState(restCoins)

        then:
        cashierPad.coinsInCashier == expectedCoins
        cashierPad.insertedCoins.isEmpty()

        where:
        insertedCoins                    | restCoins                             | expectedCoins
        [(FIVE): 2, (HALF): 4, (ONE): 1] | [(TWO): 3, (HALF): 3, (ONE_TENTH): 2] | [(FIVE): 8, (TWO): 0, (HALF): 6, (ONE): 1, (ONE_FIFTH): 5, (ONE_TENTH): 7]
    }

    def "countRestInCoinsQuantity should return rest for given amount and current coins state"() {
        given:
        cashierPad.insertedCoins = [(TWO): 3]
        and:
        def amountToPay = new BigDecimal(5.5)
        and:
        def coinsInCashier = [(FIVE): 6, (TWO): 3, (HALF): 5, (ONE_FIFTH): 5, (ONE_TENTH): 9]

        when:
        def restCoins = cashierPad.countRestInCoinsQuantity(amountToPay, coinsInCashier)

        then:
        restCoins == [(HALF): 1]
    }

    def "countRestInCoinsQuantity should throw MoneyChangeException when moneyChangeStrategy throws exception"() {
        given:
        cashierPad.insertedCoins = [(TWO): 3]
        and:
        def amountToPay = new BigDecimal(5.5)
        and:
        def coinsInCashier = [(FIVE): 6, (TWO): 3, (HALF): 5, (ONE_FIFTH): 5, (ONE_TENTH): 9]
        and:
        cashierPad.changeStrategy = Mock(MoneyChangeStrategy) {
            countRestInCoinsQuantity(*_) >> { throw new MoneyChangeException("error message") }
        }

        when:
        def restCoins = cashierPad.countRestInCoinsQuantity(amountToPay, coinsInCashier)

        then:
        thrown(MoneyChangeException)
        restCoins == null
    }

    def "insertCoinsAndReturnChange should return valid rest and update cashier state"() {
        given:
        cashierPad.coinsInCashier = [(FIVE): 6, (TWO): 3, (HALF): 5, (ONE_FIFTH): 5, (ONE_TENTH): 9]
        and:
        def amountToPay = new BigDecimal(5.5)
        and:
        cashierPad.insertedCoins = [(TWO): 3]

        when:
        def restCoins = cashierPad.payAndReturnChange(amountToPay)

        then:
        restCoins == [(HALF): 1]
        cashierPad.coinsInCashier == [(FIVE): 6, (TWO): 6, (HALF): 4, (ONE_FIFTH): 5, (ONE_TENTH): 9]
    }

    def "payAndReturnChange should throw MoneyChangeException when money changer can not count the rest and cashier state should stay unchanged"() {
        given:
        cashierPad.coinsInCashier = [(HALF): 1, (ONE_FIFTH): 5]
        and:
        cashierPad.insertedCoins = [(FIVE): 1]
        and:
        def amountToPay = new BigDecimal(1.5)

        when:
        def restCoins = cashierPad.payAndReturnChange(amountToPay)

        then:
        thrown(MoneyChangeException)
        cashierPad.coinsInCashier == [(HALF): 1, (ONE_FIFTH): 5]
        restCoins == null
    }

    def "insertCoins should properly add coins to cashier pad and return proper amount"() {
        given:
        cashierPad.insertedCoins = insertedCoins

        expect:
        expectedAmount == cashierPad.insertCoins(denominationToInsert, quantityToInsert)

        where:
        insertedCoins | expectedAmount | denominationToInsert | quantityToInsert
        [(FIVE): 1]   | 5.2            | ONE_FIFTH            | 1
        [:]           | 0.7            | ONE_TENTH            | 7
    }

    def "returnInsertedCoins should clear current state of inserted coins"() {
        given:
        cashierPad.insertedCoins = [(FIVE): 1]

        when:
        cashierPad.returnInsertedCoins()

        then:
        cashierPad.insertedCoins.isEmpty()
    }

    def "coinInserted should invoke notifyProductPrice on vendingMachineNotifier"() {
        when:
        cashierPad.coinInserted(FIVE, 1)

        then:
        1 * vendingMaichineNotifier.notifyAmountInserted(*_)
    }

    def "sufficientValueInserted should invoke notifyProductPrice on vendingMachineNotifier"() {
        given:
        cashierPad.insertedCoins = [(FIVE): 1]

        when:
        cashierPad.sufficientAmountInserted(BigDecimal.valueOf(5))

        then:
        1 * vendingMaichineNotifier.notifyRestReturned(*_)
    }
}
