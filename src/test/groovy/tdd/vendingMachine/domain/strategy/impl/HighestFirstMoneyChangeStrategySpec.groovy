package tdd.vendingMachine.domain.strategy.impl

import spock.lang.Specification
import tdd.vendingMachine.domain.strategy.MoneyChangeStrategy
import tdd.vendingMachine.domain.strategy.impl.HighestFirstMoneyChangeStrategy
import tdd.vendingMachine.exception.MoneyChangeException

import static tdd.vendingMachine.domain.Denomination.FIVE
import static tdd.vendingMachine.domain.Denomination.HALF
import static tdd.vendingMachine.domain.Denomination.ONE
import static tdd.vendingMachine.domain.Denomination.ONE_FIFTH
import static tdd.vendingMachine.domain.Denomination.ONE_TENTH
import static tdd.vendingMachine.domain.Denomination.TWO

/**
 * @author kdkz
 */
class HighestFirstMoneyChangeStrategySpec extends Specification {

    private MoneyChangeStrategy changer

    def setup() {
        changer = new HighestFirstMoneyChangeStrategy()
    }

    def "getRestInCoins should return properly counted rest with highest possible denominations"() {
        given:
        def coins = [(FIVE): 6, (TWO): 3, (ONE): 3, (HALF): 5, (ONE_FIFTH): 5, (ONE_TENTH): 9]
        and:
        def amount = new BigDecimal(8.7);

        when:
        def rest = changer.countRestInCoinsQuantity(amount, coins)

        then:
        rest == [(FIVE): 1, (TWO): 1, (ONE): 1, (HALF): 1, (ONE_FIFTH): 1]
    }

    def "getRestInCoins should throw exception for amount that could not be changed"() {
        given:
        def coins = [(FIVE): 6, (TWO): 3, (HALF): 5, (ONE): 3, (ONE_FIFTH): 0, (ONE_TENTH): 1]
        and:
        def amount = new BigDecimal(8.7);

        when:
        def rest = changer.countRestInCoinsQuantity(amount, coins)

        then:
        thrown(MoneyChangeException)
    }
}
