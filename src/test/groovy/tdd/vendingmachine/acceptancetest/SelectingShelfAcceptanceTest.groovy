package tdd.vendingmachine.acceptancetest

import spock.lang.Specification
import tdd.vendingmachine.domain.VendingMachineFacade
import tdd.vendingmachine.domain.VendingMachineTestConfiguration
import tdd.vendingmachine.testutility.Randomizer

import static tdd.vendingmachine.testutility.VendingMachineTestBuilder.aVendingMachine

class SelectingShelfAcceptanceTest extends Specification {

    private VendingMachineFacade sut = VendingMachineTestConfiguration.vendingMachineFacade()

    def "selecting a shelf's number should show the shelf's product's price"() {
        given: "exists Vending Machine containing a shelf with the product type"
            String shelfNumber = Randomizer.aShelfNumber()
            BigDecimal priceValue = 1.5
            sut.createVendingMachine(aVendingMachine().containingShelfWithProductTypePriceValue(shelfNumber, priceValue)
                                                      .build())
        when: "one selects a shelf number"
            sut.selectShelfNumber(shelfNumber)
        then: "the display shows price of product on that shelf"
            sut.readDisplay() == '1.50'
    }
    
    def "selecting a non-existing shelf's number should show a warning message"() {
        given: "exists Vending Machine not containing a specific shelf"
            String shelfNumber = Randomizer.aShelfNumber()
            sut.createVendingMachine(aVendingMachine().notContainingShelf(shelfNumber)
                                                      .build())
        when: "one selects a non-existing shelf number"
            sut.selectShelfNumber(shelfNumber)
        then: "the display shows a warning message"
            sut.readDisplay() == "No shelf: #${shelfNumber}"
    }
    
    def "selecting a shelf's number with no products on it should show a warning message"() {
        given: "exists Vending Machine with no products on a specific shelf"
            String shelfNumber = Randomizer.aShelfNumber()
            sut.createVendingMachine(aVendingMachine().containingShelfWithNoProductsOnIt(shelfNumber)
                                                      .build())
        when: "one selects a shelf's number with no products on the shelf"
            sut.selectShelfNumber(shelfNumber)
        then: "the display shows a warning message"
            sut.readDisplay() == "Empty shelf: #${shelfNumber}"
    }
    
    def "selecting a new shelf after a shelf has already been selected should show the new shelf's product's price"() {
        given: "exists Vending Machine containing a shelf with the product type"
            String shelfNumber = Randomizer.aShelfNumber()
            String newShelfNumber = Randomizer.aDifferentShelfNumber(shelfNumber)
            BigDecimal priceValue = 1.5
            BigDecimal newPriceValue = 3.5
            sut.createVendingMachine(aVendingMachine().containingShelfWithProductTypePriceValue(shelfNumber, priceValue)
                                                      .containingShelfWithProductTypePriceValue(newShelfNumber, newPriceValue)
                                                      .build())
        and: "one has selected a shelf number"
            sut.selectShelfNumber(shelfNumber)
        when: "one selects a new different shelf number"
            sut.selectShelfNumber(newShelfNumber)
        then: "the display shows a different new price of product on that shelf"
            sut.readDisplay() == '3.50'
    }
}
