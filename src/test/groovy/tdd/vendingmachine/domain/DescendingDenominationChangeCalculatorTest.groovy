package tdd.vendingmachine.domain

import spock.lang.Shared
import spock.lang.Specification
import tdd.vendingmachine.domain.dto.CoinDto

class DescendingDenominationChangeCalculatorTest extends Specification {
    
    @Shared
    private DescendingDenominationChangeCalculator changeCalculator = new DescendingDenominationChangeCalculator()
    
    def "should calculate change using the highest denomination first"() {
        given:
            Coins availableCoins = Coins.create(availableCoinsDtos)
            Money changeMoney = new MoneyAmount(changeValue)
        when:
            Optional<Change> optionalChange = changeCalculator.calculate(availableCoins, changeMoney)
        then:
            optionalChange.isPresent()
        and:
            Change change = optionalChange.get()
            change.coins().toDto().sort() == expectedChangeCoins.sort()
        where:
            availableCoinsDtos                                             | changeValue || expectedChangeCoins
            cs(0.5, 2) + cs(1.0, 1)                                        | 1.0         || cs(1.0, 1)
            cs(0.1, 5) + cs(0.2, 2) + cs(1.0, 1)                           | 0.5         || cs(0.2, 2) + cs(0.1, 1)
            cs(0.1, 5) + cs(0.2, 1) + cs(0.5, 1) + cs(1.0, 1) + cs(2.0, 1) | 4.0         || cs(0.1, 3) + cs(0.2, 1) + cs(0.5, 1) + cs(1.0, 1) + cs(2.0, 1)
            []                                                             | 0.0         || []
    }
    
    def "should not calculate change if cannot compose change value from available coins"() {
        given:
            Coins availableCoins = Coins.create(availableCoinsDtos)
            Money changeMoney = new MoneyAmount(changeValue)
        when:
            Optional<Change> optionalChange = changeCalculator.calculate(availableCoins, changeMoney)
        then:
            !optionalChange.isPresent()
        where:
            availableCoinsDtos      | changeValue
            cs(0.2, 4) + cs(2.0, 1) | 1.0
            []                      | 0.1
            cs(2.0, 3)              | 5.0
    }
    
    static List<CoinDto> cs(BigDecimal denomination, int amount) {
        assert amount > 0
        return (1..amount).collect { new CoinDto(denomination) }
    }
}
