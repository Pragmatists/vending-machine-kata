package tdd.vendingmachine.testutility

import tdd.vendingmachine.domain.dto.ProductTypeDto
import tdd.vendingmachine.domain.dto.ShelfDto

import static tdd.vendingmachine.testutility.ProductTypeTestBuilder.aProductType

class ShelfTestBuilder {

    private String number
    private ProductTypeDto productType
    private int productCount

    private ShelfTestBuilder() {
        number = Randomizer.aShelfNumber()
        productCount = Randomizer.aNotEmptyProductCount()
        productType = aProductType().build()
    }

    static ShelfTestBuilder aShelf() {
        new ShelfTestBuilder()
    }
    
    ShelfDto build() {
        return new ShelfDto(number, productType, productCount)
    }
    
    ShelfTestBuilder withNumber(String number) {
        this.number = number
        return this
    }

    ShelfTestBuilder withPrice(BigDecimal priceValue) {
        productType = aProductType().withPrice(priceValue)
                                    .build()
        return this
    }
    
    ShelfTestBuilder withProductType(ProductTypeDto productType) {
        this.productType = productType
        return this
    }
    
    ShelfTestBuilder withProductCount(int productCount) {
        this.productCount = productCount
        return this
    }
}
