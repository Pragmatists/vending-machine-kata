package tdd.vendingMachine

import spock.lang.Specification
import tdd.vendingMachine.exception.MoneyChangeException

import static tdd.vendingMachine.Denomination.FIVE
import static tdd.vendingMachine.Denomination.HALF
import static tdd.vendingMachine.Denomination.ONE
import static tdd.vendingMachine.Denomination.ONE_FIFTH
import static tdd.vendingMachine.Denomination.ONE_TENTH
import static tdd.vendingMachine.Denomination.TWO

/**
 * @author kdkz
 */
class CashierPadSpec extends Specification {

    private CashierPad cashierPad

    def setup() {
        cashierPad = new CashierPad()
    }

    def "getMoneyInCashier should return the whole amount of money in cash"() {
        given:
        cashierPad.coinsInCashier = coinsInCashier

        when:
        def amount = cashierPad.getMoneyInCashier()

        then:
        amount == BigDecimal.valueOf(amount)

        where:
        sum  | coinsInCashier
        43.4 | [(FIVE): 6, (TWO): 3, (HALF): 5, (ONE): 3, (ONE_FIFTH): 5, (ONE_TENTH): 9]
        42.4 | [(FIVE): 6, (TWO): 3, (HALF): 5, (ONE): 3, (ONE_TENTH): 9]
        0    | [:]
    }

    def "getDenominationQuantity should return proper quantity for given denomination"() {
        given:
        cashierPad.coinsInCashier = coinsInCashier

        when:
        def quantity = cashierPad.getDenominationQuantity(denomination)
        then:
        quantity == expectedQuantity

        where:
        denomination | expectedQuantity | coinsInCashier
        FIVE         | 6                | [(FIVE): 6, (TWO): 3, (HALF): 5, (ONE): 3, (ONE_FIFTH): 5, (ONE_TENTH): 9]
        ONE_FIFTH    | 0                | [(FIVE): 6, (TWO): 3, (HALF): 5, (ONE): 3, (ONE_TENTH): 9]
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
        when:
        def summedCoinsQuantity = cashierPad.sumInsertedCoinsWithCoinsInCashier(insertedCoins, coinsQuantity)

        then:
        summedCoinsQuantity == expectedCoins

        where:
        insertedCoins                    | coinsQuantity                                                    | expectedCoins
        [(FIVE): 2, (HALF): 4, (ONE): 1] | [(FIVE): 6, (TWO): 3, (HALF): 5, (ONE_FIFTH): 5, (ONE_TENTH): 9] | [(FIVE): 8, (TWO): 3, (HALF): 9, (ONE): 1, (ONE_FIFTH): 5, (ONE_TENTH): 9]
    }

    def "changeCashierState should change state of cashier pad by given inserted coins quantity and rest coins quantity"() {
        given:
        cashierPad.coinsInCashier = [(FIVE): 6, (TWO): 3, (HALF): 5, (ONE_FIFTH): 5, (ONE_TENTH): 9]

        when:
        cashierPad.changeCashierState(insertedCoins, restCoins)

        then:
        cashierPad.coinsInCashier == expectedCoins

        where:
        insertedCoins                    | restCoins                             | expectedCoins
        [(FIVE): 2, (HALF): 4, (ONE): 1] | [(TWO): 3, (HALF): 3, (ONE_TENTH): 2] | [(FIVE): 8, (TWO): 0, (HALF): 6, (ONE): 1, (ONE_FIFTH): 5, (ONE_TENTH): 7]
    }

    def "countRestInCoinsQuantity should return rest for given amount and current coins state"() {
        given:
        def amountToPay = new BigDecimal(5.5)
        and:
        def coinsInCashier = [(FIVE): 6, (TWO): 3, (HALF): 5, (ONE_FIFTH): 5, (ONE_TENTH): 9]
        def insertedCoins = [(TWO): 3]
        and:
        def moneyChangeStrategy = Mock(MoneyChangeStrategy) {
            countRestInCoinsQuantity(*_) >> [(HALF): 1]
        }

        when:
        def restCoins = cashierPad.countRestInCoinsQuantity(amountToPay, insertedCoins, coinsInCashier, moneyChangeStrategy)

        then:
        restCoins == [(HALF): 1]
    }

    def "countRestInCoinsQuantity should throw MoneyChangeException when moneyChangeStrategy throws exception"() {
        given:
        def amountToPay = new BigDecimal(5.5)
        and:
        def coinsInCashier = [(FIVE): 6, (TWO): 3, (HALF): 5, (ONE_FIFTH): 5, (ONE_TENTH): 9]
        def insertedCoins = [(TWO): 3]
        and:
        def moneyChangeStrategy = Mock(MoneyChangeStrategy) {
            countRestInCoinsQuantity(*_) >> { throw new MoneyChangeException("error message") }
        }

        when:
        def restCoins = cashierPad.countRestInCoinsQuantity(amountToPay, insertedCoins, coinsInCashier, moneyChangeStrategy)

        then:
        thrown(MoneyChangeException)
        restCoins == null
    }

}
