package tdd.vendingmachine.acceptancetest

import spock.lang.Specification
import tdd.vendingmachine.domain.VendingMachineFacade
import tdd.vendingmachine.domain.VendingMachineTestConfiguration
import tdd.vendingmachine.domain.dto.CoinDto
import tdd.vendingmachine.domain.dto.ProductDto
import tdd.vendingmachine.domain.dto.ProductTypeDto
import tdd.vendingmachine.testutility.Randomizer

import static tdd.vendingmachine.testutility.ProductTypeTestBuilder.aProductType
import static tdd.vendingmachine.testutility.ShelfTestBuilder.aShelf
import static tdd.vendingmachine.testutility.VendingMachineTestBuilder.aVendingMachine

class BuyingProductAcceptanceTest extends Specification {
    
    private VendingMachineFacade sut = VendingMachineTestConfiguration.vendingMachineFacade()
    
    def "inserting more than enough money should release the product and give the change"() {
        given: "exists Vending Machine containing a shelf with the product type"
            String shelfNumber = Randomizer.aShelfNumber()
            ProductTypeDto productType = aProductType().withPrice(3.2)
                                                       .build()
            sut.createVendingMachine(aVendingMachine().containingShelfWithProductType(shelfNumber, productType)
                                                      .build())
        and: "one has selected shelf number with that product"
            sut.selectShelfNumber(shelfNumber)
        when: "one inserts more than enough money to buy the product"
            sut.insertCoin(new CoinDto(5.0))
        then: "the product can be taken"
            sut.takeProducts() == [new ProductDto(productType.name)]
        and: "the change can be taken"
            sut.takeChange().sort() == [
                new CoinDto(1.0),
                new CoinDto(0.5),
                new CoinDto(0.2),
                new CoinDto(0.1)
            ].sort()
        and: "the display is cleared"
            sut.readDisplay() == ''
    }
    
    def "inserting enough money should release the product"() {
        given: "exists Vending Machine containing a shelf with the product type"
            String shelfNumber = Randomizer.aShelfNumber()
            ProductTypeDto productType = aProductType().withPrice(2.0)
                                                       .build()
            sut.createVendingMachine(aVendingMachine().containingShelfWithProductType(shelfNumber, productType)
                                                      .build())
        and: "one has selected shelf number with that product"
            sut.selectShelfNumber(shelfNumber)
        when: "one inserts exactly enough money to buy the product"
            sut.insertCoin(new CoinDto(2.0))
        then: "the product can be taken"
            sut.takeProducts() == [new ProductDto(productType.name)]
        and: "the display is cleared"
            sut.readDisplay() == ''
    }

    def "should not be able to buy product if change cannot be given"() {
        given: "exists Vending Machine with no money"
            String shelfNumber = Randomizer.aShelfNumber()
            ProductTypeDto productType = aProductType().withPrice(2.0)
                                                       .build()
            sut.createVendingMachine(aVendingMachine().containingShelfWithProductType(shelfNumber, productType)
                                                      .withNoCoins()
                                                      .build())
        and: "one has selected shelf number with a product"
            sut.selectShelfNumber(shelfNumber)
        when: "one inserts enough money to buy the product, but machine is not able to return the change"
            sut.insertCoin(new CoinDto(5.0))
        then: "the display shows a warning message"
            sut.readDisplay() == 'No change'
        and: "the inserted money can be taken as change"
            sut.takeChange() == [new CoinDto(5.0)]
        and: "the product is not returned and thus cannot be taken"
            sut.takeProducts() == []
    }
    
    def "should not be able to buy product if change cannot be given, even if the price is met"() {
        given: "exists Vending Machine with no money"
            String shelfNumber = Randomizer.aShelfNumber()
            ProductTypeDto productType = aProductType().withPrice(2.0)
                                                       .build()
            sut.createVendingMachine(aVendingMachine().containingShelfWithProductType(shelfNumber, productType)
                                                      .withNoCoins()
                                                      .build())
        and: "one has selected shelf number with a product"
            sut.selectShelfNumber(shelfNumber)
        and: "one has inserted a not enough money"
            sut.insertCoin(new CoinDto(1.0))
        when: "one inserts enough money to buy the product, but machine is not able to return the change"
            sut.insertCoin(new CoinDto(5.0))
        then: "the display shows a warning message"
            sut.readDisplay() == 'No change'
        and: "the inserted money can be taken as change"
            sut.takeChange().sort() == [new CoinDto(1.0), new CoinDto(5.0)].sort()
        and: "the product is not returned and thus cannot be taken"
            sut.takeProducts() == []
    }
    
    def "inserting more than enough money should release the product and return the change if possible"() {
        given: "exists Vending Machine with no money"
            String shelfNumber = Randomizer.aShelfNumber()
            ProductTypeDto productType = aProductType().withPrice(2.0)
                                                       .build()
            sut.createVendingMachine(aVendingMachine().containingShelfWithProductType(shelfNumber, productType)
                                                      .withNoCoins()
                                                      .build())
        and: "one has selected shelf number with a product"
            sut.selectShelfNumber(shelfNumber)
        and: "one has inserted a not enough money"
            sut.insertCoin(new CoinDto(1.0))
        when: "one inserts enough money to buy the product"
            sut.insertCoin(new CoinDto(2.0))
        and: "the product can be taken"
            sut.takeProducts() == [new ProductDto(productType.name)]
        and: "the inserted money, exceeding the price, can be taken as change"
            sut.takeChange() == [new CoinDto(1.0)]
        then: "the display is cleared"
            sut.readDisplay() == ''
    }
    
    def "should vending machine be able to gather money and be able to use it as the change"() {
        given: "exists Vending Machine with no money"
            String shelfNumber = Randomizer.aShelfNumber()
            ProductTypeDto productType = aProductType().withPrice(1.0)
                                                       .build()
            sut.createVendingMachine(aVendingMachine().withNoCoins()
                                                      .containingShelf(aShelf().withNumber(shelfNumber)
                                                                               .withProductType(productType)
                                                                               .withProductCount(2)
                                                                               .build())
                                                      .build())
        and: "firstly, one has selected shelf number with that product, but inserted the unchangeable coin"
            sut.selectShelfNumber(shelfNumber)
            sut.insertCoin(new CoinDto(2.0))
        and: "the display shows warning message, coin can be taken as change and the product is not returned"
            assert sut.readDisplay() == 'No change'
            assert sut.takeChange() == [new CoinDto(2.0)]
            assert sut.takeProducts() == []
        and: "secondly, one has selected shelf number with that product, inserted the exact price, and took products"
            sut.selectShelfNumber(shelfNumber)
            sut.insertCoin(new CoinDto(1.0))
            assert sut.takeProducts() == [new ProductDto(productType.name)]
        and: "then one has selected shelf number with that product again"
            sut.selectShelfNumber(shelfNumber)
        when: "one inserts coin, that was previously unchangeable"
            sut.insertCoin(new CoinDto(2.0))
        then: "the product can now be taken"
            sut.takeProducts() == [new ProductDto(productType.name)]
        and: "the change can be taken"
            sut.takeChange() == [new CoinDto(1.0)]
        and: "the display is cleared"
            sut.readDisplay() == ''
    }
    
    def "should product and change dispensers be able to accumulate things, but be cleared after dispensing"() {
        given: "exists Vending Machine containing a shelf with the product type"
            String shelfNumber = Randomizer.aShelfNumber()
            ProductTypeDto productType = aProductType().withPrice(2.0)
                                                       .build()
            sut.createVendingMachine(aVendingMachine().containingShelf((aShelf().withNumber(shelfNumber)
                                                                                .withProductType(productType)
                                                                                .withProductCount(2)
                                                                                .build()))
                                                      .build())
        and: "one has bought the product with more than enough money, but didn't collect the product or the change"
            sut.selectShelfNumber(shelfNumber)
            sut.insertCoin(new CoinDto(5.0))
        and: "one has bought that product with more than enough money, again"
            sut.selectShelfNumber(shelfNumber)
            sut.insertCoin(new CoinDto(5.0))
        when: "one takes the product and change"
            Collection<ProductDto> products = sut.takeProducts()
            Collection<CoinDto> change = sut.takeChange()
        then: "the products are accumulated"
            products == [new ProductDto(productType.name), new ProductDto(productType.name)]
        and: "the change is accumulated, too"
            change.sort() == [new CoinDto(2.0), new CoinDto(2.0), new CoinDto(1.0), new CoinDto(1.0)].sort()
        and: "dispensing products again results in nothing"
            sut.takeProducts() == []
        and: "dispensing change again results in nothing"
            sut.takeChange() == []
    }
    
    def "should product be taken off the shelf after buying it"() {
        given: "exists Vending Machine containing a shelf with the product type with the product count = 1"
            String shelfNumber = Randomizer.aShelfNumber()
            ProductTypeDto productType = aProductType().withPrice(2.0)
                                                       .build()
            int productCount = 1
            sut.createVendingMachine(aVendingMachine().containingShelf((aShelf().withNumber(shelfNumber)
                                                                                .withProductType(productType)
                                                                                .withProductCount(productCount)
                                                                                .build()))
                                                      .build())
        and: "one has bought the last product of that shelf"
            sut.selectShelfNumber(shelfNumber)
            sut.insertCoin(new CoinDto(2.0))
        when: "one selects the same shelf's number"
            sut.selectShelfNumber(shelfNumber)
        then: "the display shows a warning message"
            sut.readDisplay() == "Empty shelf: #${shelfNumber}"
    }
}
