package tdd.vendingmachine.domain

import spock.lang.Specification
import tdd.vendingmachine.domain.dto.CoinDto
import tdd.vendingmachine.testutility.Randomizer

class ChangeDispenserTest extends Specification {
    
    def "should empty dispenser after dispensing coins"() {
        given:
            ChangeDispenser changeDispenser = ChangeDispenser.empty()
        and:
            CoinDto coinDto = Randomizer.aCoin()
            changeDispenser.put(Coin.create(coinDto))
        expect: 'dispensing for the first time should return inserted coins'
            changeDispenser.dispense() == [coinDto]
        when: 'dispensing for the second time'
            Collection<CoinDto> dispensedCoins = changeDispenser.dispense()
        then: 'dispenser should be empty and return no coins'
            dispensedCoins.empty
    }
    
}
