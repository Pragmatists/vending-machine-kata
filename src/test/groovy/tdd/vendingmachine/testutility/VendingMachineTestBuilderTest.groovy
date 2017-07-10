package tdd.vendingmachine.testutility

import spock.lang.Specification
import tdd.vendingmachine.domain.dto.CoinDto
import tdd.vendingmachine.domain.dto.VendingMachineDto

import static tdd.vendingmachine.testutility.VendingMachineTestBuilder.aVendingMachine

class VendingMachineTestBuilderTest extends Specification {
    
    def "should default vending machine have 5 coins of each acceptable denomination"() {
        given:
            VendingMachineTestBuilder vendingMachineTestBuilder = aVendingMachine()
        when:
            VendingMachineDto vendingMachineDto = vendingMachineTestBuilder.build()
        then:
            Map<BigDecimal, List<CoinDto>> coinsByDenominations = vendingMachineDto.coins.groupBy { it.value }
            coinsByDenominations.keySet() == vendingMachineDto.acceptableDenominations
            coinsByDenominations.every { it.value.size() == 5 }
    }
}
