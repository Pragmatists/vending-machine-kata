package tdd.vendingmachine.testutility

import tdd.vendingmachine.domain.dto.ProductTypeDto
import tdd.vendingmachine.domain.dto.ShelfDto

import static tdd.vendingmachine.testutility.ProductTypeTestBuilder.aProductType

class ShelfTestBuilder {

    private String number
    private BigDecimal priceValue
    private int productCount

    private ShelfTestBuilder() {
        number = Randomizer.aShelfNumber()
        priceValue = Randomizer.aPriceValue()
        productCount = Randomizer.aNotEmptyProductCount()
    }

    static ShelfTestBuilder aShelf() {
        new ShelfTestBuilder()
    }

    ShelfTestBuilder withNumber(String number) {
        this.number = number
        return this
    }

    ShelfTestBuilder withPrice(BigDecimal priceValue) {
        this.priceValue = priceValue
        return this
    }

    ShelfDto build() {
        ProductTypeDto productType = aProductType().withPrice(priceValue)
                                                   .build()
        return new ShelfDto(number, productType, productCount)
    }
}
