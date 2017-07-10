package tdd.vendingmachine.testutility

import tdd.vendingmachine.domain.dto.CoinDto
import tdd.vendingmachine.domain.dto.ProductTypeDto
import tdd.vendingmachine.domain.dto.ShelfDto
import tdd.vendingmachine.domain.dto.VendingMachineDto

import static tdd.vendingmachine.testutility.ShelfTestBuilder.aShelf

class VendingMachineTestBuilder {

    private Map<String, ShelfDto> shelvesByNumber
    private Set<BigDecimal> acceptableDenominations
    private Map<BigDecimal, List<CoinDto>> coinsByDenomination

    private VendingMachineTestBuilder() {
        shelvesByNumber = ShelvesObjectMother.shelvesByNumber()
        acceptableDenominations = [0.1, 0.2, 0.5, 1.0, 2.0, 5.0]
        coinsByDenomination = initialCoins(acceptableDenominations)
    }

    static Map<BigDecimal, List<CoinDto>> initialCoins(Set<BigDecimal> acceptableDenominations) {
        return acceptableDenominations.collectEntries { denomination ->
            return [(denomination): (1..5).collect { return new CoinDto(denomination) }]
        }
    }

    static VendingMachineTestBuilder aVendingMachine() {
        return new VendingMachineTestBuilder()
    }
    
    VendingMachineDto build() {
        return new VendingMachineDto(new HashSet<>(shelvesByNumber.values()), acceptableDenominations,
            coinsByDenomination.values().flatten())
    }

    VendingMachineTestBuilder containingShelfWithProductTypePriceValue(String shelfNumber, BigDecimal price) {
        return containingShelf(aShelf().withNumber(shelfNumber)
                                       .withPrice(price)
                                       .build())
    }

    VendingMachineTestBuilder notContainingShelf(String shelfNumber) {
        shelvesByNumber.remove(shelfNumber)
        return this
    }
    
    VendingMachineTestBuilder containingShelfWithNoProductsOnIt(String shelfNumber) {
        ShelfDto shelfDto = Optional.ofNullable(shelvesByNumber.get(shelfNumber))
                                    .orElseGet {
                                        ShelfDto newShelfDto = aShelf().withNumber(shelfNumber).build()
                                        shelvesByNumber.put(newShelfDto.shelfNumber, newShelfDto)
                                        return newShelfDto
                                    }
        shelfDto.productCount = 0
        return this
    }
    
    VendingMachineTestBuilder containingShelf(String shelfNumber) {
        return containingShelfWithProductTypePriceValue(shelfNumber, Randomizer.aPriceValue())
    }
    
    VendingMachineTestBuilder notAcceptingDenomination(BigDecimal denomination) {
        acceptableDenominations.remove(denomination)
        coinsByDenomination.remove(denomination)
        return this
    }
    
    VendingMachineTestBuilder containingShelfWithProductType(String shelfNumber, ProductTypeDto productTypeDto) {
        return containingShelf(aShelf().withNumber(shelfNumber)
                                       .withProductType(productTypeDto)
                                       .build())
    }
    
    VendingMachineTestBuilder containingShelf(ShelfDto shelf) {
        shelvesByNumber.put(shelf.shelfNumber, shelf)
        return this
    }
    
    VendingMachineTestBuilder withNoCoins() {
        coinsByDenomination.clear()
        return this
    }
}
