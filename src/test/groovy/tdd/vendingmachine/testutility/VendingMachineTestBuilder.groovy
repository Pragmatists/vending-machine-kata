package tdd.vendingmachine.testutility

import tdd.vendingmachine.domain.dto.ShelfDto
import tdd.vendingmachine.domain.dto.VendingMachineDto

import static tdd.vendingmachine.testutility.ShelfTestBuilder.aShelf

class VendingMachineTestBuilder {

    private Map<String, ShelfDto> shelvesByNumber
    private Set<BigDecimal> acceptableDenominations

    private VendingMachineTestBuilder() {
        shelvesByNumber = ShelvesObjectMother.shelvesByNumber()
        acceptableDenominations = [0.1, 0.2, 0.5, 1.0, 2.0, 5.0]
    }

    static VendingMachineTestBuilder aVendingMachine() {
        return new VendingMachineTestBuilder()
    }
    
    VendingMachineDto build() {
        return new VendingMachineDto(new HashSet<>(shelvesByNumber.values()), acceptableDenominations)
    }

    VendingMachineTestBuilder containingShelfWithProductTypePriceValue(String shelfNumber, BigDecimal price) {
        ShelfDto shelf = aShelf().withNumber(shelfNumber)
                                 .withPrice(price)
                                 .build()
        shelvesByNumber.put(shelf.shelfNumber, shelf)
        return this
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
        return this
    }
}
