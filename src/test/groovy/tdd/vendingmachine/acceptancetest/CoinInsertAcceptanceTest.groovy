package tdd.vendingmachine.acceptancetest

import spock.lang.Specification
import tdd.vendingmachine.domain.VendingMachineFacade
import tdd.vendingmachine.domain.VendingMachineTestConfiguration
import tdd.vendingmachine.domain.dto.CoinDto
import tdd.vendingmachine.testutility.Randomizer

import static tdd.vendingmachine.testutility.VendingMachineTestBuilder.aVendingMachine

class CoinInsertAcceptanceTest extends Specification {
    
    private VendingMachineFacade sut = VendingMachineTestConfiguration.vendingMachineFacade()
    
    def "inserting a coin should show the amount left to buy a product"() {
        given: "exists Vending Machine containing a shelf with the product type"
            String shelfNumber = Randomizer.aShelfNumber()
            BigDecimal priceValue = 2.5
            sut.createVendingMachine(aVendingMachine().containingShelfWithProductTypePriceValue(shelfNumber, priceValue)
                                                      .build())
        and: "one has selected shelf number with a product"
            sut.selectShelfNumber(shelfNumber)
        when: "one inserts a coin"
            sut.insertCoin(new CoinDto(1.0))
        then: "the display shows amount that must be added to cover the product's price"
            sut.readDisplay() == '1.50'
    }

    def "inserting multiple coins should show the reducing amount left to buy a product"() {
        given: "exists Vending Machine containing a shelf with the product type"
            String shelfNumber = Randomizer.aShelfNumber()
            BigDecimal priceValue = 3.3
            sut.createVendingMachine(aVendingMachine().containingShelfWithProductTypePriceValue(shelfNumber, priceValue)
                                                      .build())
        and: "one has selected shelf number with a product"
            sut.selectShelfNumber(shelfNumber)
        when: "one inserts a coin"
            sut.insertCoin(new CoinDto(0.2))
        then: "the display shows amount that must be added to cover the product's price"
            sut.readDisplay() == '3.10'
        when: "one inserts another coin"
            sut.insertCoin(new CoinDto(2))
        then: "the display shows the reduced amount that must be added to cover the product's price"
            sut.readDisplay() == '1.10'
    }
    
    def "inserting a coin before selecting a shelf number should show a warning message and return the coin as change"() {
        given: "exists Vending Machine"
            sut.createVendingMachine(aVendingMachine().build())
        when: "one inserts a coin before selecting shelf number"
            sut.insertCoin(new CoinDto(1.00))
        then: "the display shows a warning message"
            sut.readDisplay() == 'Select product first'
        and: "the coin is returned as change"
            sut.takeChange() == [new CoinDto(1.00)]
    }
    
    def "selecting a shelf number after inserting a coin should do nothing"() {
        given: "exists Vending Machine containing a shelf with the product type"
            String shelfNumber = Randomizer.aShelfNumber()
            BigDecimal priceValue = 2.5
            sut.createVendingMachine(aVendingMachine().containingShelfWithProductTypePriceValue(shelfNumber, priceValue)
                                                      .build())
        and: "one has selected shelf number with a product"
            sut.selectShelfNumber(shelfNumber)
        and: "one has inserted a coin"
            sut.insertCoin(new CoinDto(1.0))
        expect: "the display shows amount that must be added to cover the product's price"
            sut.readDisplay() == '1.50'
        when: "one selects another shelf number"
            sut.selectShelfNumber(Randomizer.aShelfNumber())
        then: "the display still shows the same amount as before"
            sut.readDisplay() == '1.50'
    }

    def "inserting a coin that is not acceptable should show a warning message and return the coin as change"() {
        given: "exists Vending Machine with a set of acceptable acceptableDenominations"
            String shelfNumber = Randomizer.aShelfNumber()
            BigDecimal notAcceptableDenomination = Randomizer.aDenomination()
            sut.createVendingMachine(aVendingMachine().containingShelf(shelfNumber)
                                                      .notAcceptingDenomination(notAcceptableDenomination)
                                                      .build())
        and: "one has selected shelf number with a product"
            sut.selectShelfNumber(shelfNumber)
        when: "one inserts a non-acceptable coin"
            sut.insertCoin(new CoinDto(notAcceptableDenomination))
        then: "the display shows a warning message"
            sut.readDisplay() == 'Coin not acceptable'
        and: "the inserted money can be taken as change"
            sut.takeChange() == [new CoinDto(notAcceptableDenomination)]
    }
}
