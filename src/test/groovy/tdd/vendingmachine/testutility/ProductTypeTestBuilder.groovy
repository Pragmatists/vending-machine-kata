package tdd.vendingmachine.testutility

import tdd.vendingmachine.domain.dto.PriceDto
import tdd.vendingmachine.domain.dto.ProductTypeDto

import static tdd.vendingmachine.testutility.PriceTestBuilder.aPrice

class ProductTypeTestBuilder {

    private String name
    private BigDecimal priceValue

    private ProductTypeTestBuilder() {
        name = Randomizer.aProductTypeName()
        priceValue = Randomizer.aPriceValue()
    }

    static ProductTypeTestBuilder aProductType() {
        return new ProductTypeTestBuilder()
    }


    ProductTypeTestBuilder withPrice(BigDecimal priceValue) {
        this.priceValue = priceValue
        return this
    }

    ProductTypeDto build() {
        PriceDto price = aPrice().withValue(priceValue)
                                 .build()
        return new ProductTypeDto(name, price)
    }
}
