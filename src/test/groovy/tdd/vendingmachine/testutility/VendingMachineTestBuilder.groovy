package tdd.vendingmachine.testutility

import tdd.vendingmachine.domain.dto.ShelfDto
import tdd.vendingmachine.domain.dto.VendingMachineDto

import static tdd.vendingmachine.testutility.ShelfTestBuilder.aShelf

class VendingMachineTestBuilder {

    private Map<String, ShelfDto> shelvesByNumber

    private VendingMachineTestBuilder() {
        shelvesByNumber = ShelvesObjectMother.shelvesByNumber()
    }

    static VendingMachineTestBuilder aVendingMachine() {
        new VendingMachineTestBuilder()
    }
    
    VendingMachineDto build() {
        return new VendingMachineDto(new HashSet<>(shelvesByNumber.values()))
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
}
