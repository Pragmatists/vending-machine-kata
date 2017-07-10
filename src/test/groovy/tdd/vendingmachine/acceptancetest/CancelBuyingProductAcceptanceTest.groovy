package tdd.vendingmachine.acceptancetest

import spock.lang.Specification
import tdd.vendingmachine.domain.VendingMachineFacade
import tdd.vendingmachine.domain.VendingMachineTestConfiguration
import tdd.vendingmachine.domain.dto.CoinDto
import tdd.vendingmachine.domain.dto.ProductTypeDto
import tdd.vendingmachine.testutility.Randomizer

import static tdd.vendingmachine.testutility.ProductTypeTestBuilder.aProductType
import static tdd.vendingmachine.testutility.VendingMachineTestBuilder.aVendingMachine

class CancelBuyingProductAcceptanceTest extends Specification {
    
    private VendingMachineFacade sut = VendingMachineTestConfiguration.vendingMachineFacade()
    
    def "should return the inserted money after canceling the transaction"() {
        given: "exists Vending Machine containing a shelf with the product type"
            String shelfNumber = Randomizer.aShelfNumber()
            ProductTypeDto productType = aProductType().withPrice(3.2)
                                                       .build()
            sut.createVendingMachine(aVendingMachine().containingShelfWithProductType(shelfNumber, productType)
                                                      .build())
        
        and: "one has selected shelf number with that product"
            sut.selectShelfNumber(shelfNumber)
        and: "one has inserted insufficient amount of money to buy the product"
            sut.insertCoin(new CoinDto(2.0))
            sut.insertCoin(new CoinDto(1.0))
            sut.insertCoin(new CoinDto(0.1))
        when: "one presses 'Cancel'"
            sut.cancel()
        then: "the inserted money can be taken as change"
            sut.takeChange().sort() == [new CoinDto(2.0), new CoinDto(1.0), new CoinDto(0.1)].sort()
        and: "the product is not returned and thus cannot be taken"
            sut.takeProducts() == []
        and: "the display is cleared"
            sut.readDisplay() == ''
    }
    
    def "should clear the shelf choice after canceling the transaction"() {
        given: "exists Vending Machine containing a shelf with a product"
            String shelfNumber = Randomizer.aShelfNumber()
            sut.createVendingMachine(aVendingMachine().containingShelf(shelfNumber)
                                                      .build())
        and: "one has selected shelf number with that product"
            sut.selectShelfNumber(shelfNumber)
        when: "one presses 'Cancel'"
            sut.cancel()
        then: "the display is cleared"
            sut.readDisplay() == ''
    }
    
    def "should canceling the transaction do nothing if no transaction is under way"() {
        given: "exists idle Vending Machine"
            sut.createVendingMachine(aVendingMachine().build())
        when: "one presses 'Cancel'"
            sut.cancel()
        then: "the display is cleared"
            sut.readDisplay() == ''
    }
}
