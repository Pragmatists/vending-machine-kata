package tdd.vendingMachine

import spock.lang.Specification
import tdd.vendingMachine.exception.MoneyChangeException

import static Denomination.FIVE
import static Denomination.HALF
import static Denomination.ONE
import static Denomination.ONE_FIFTH
import static Denomination.ONE_TENTH
import static Denomination.TWO

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
        def coins = [(FIVE):6, (TWO):3, (HALF):5, (ONE): 3, (ONE_FIFTH): 5, (ONE_TENTH):9]
        and:
        def amount = new BigDecimal(8.7);

        when:
        def rest = changer.countRestInCoinsQuantity(amount, coins)

        then:
        rest == [(FIVE): 1, (TWO): 1, (HALF): 3, (ONE_FIFTH): 1]
    }

    def "getRestInCoins should throw exception for amount that could not be changed"() {
        given:
        def coins = [(FIVE):6, (TWO):3, (HALF):5, (ONE): 3, (ONE_FIFTH): 0, (ONE_TENTH):1]
        and:
        def amount = new BigDecimal(8.7);

        when:
        def rest = changer.countRestInCoinsQuantity(amount, coins)

        then:
        thrown(MoneyChangeException)
    }
}
