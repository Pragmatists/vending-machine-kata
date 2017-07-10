package tdd.vendingmachine.testutility

import tdd.vendingmachine.domain.dto.PriceDto

class PriceTestBuilder {

    private BigDecimal value

    private PriceTestBuilder() {
        value = Randomizer.aPriceValue()
    }

    static PriceTestBuilder aPrice() {
        return new PriceTestBuilder()
    }


    PriceTestBuilder withValue(BigDecimal priceValue) {
        value = priceValue
        return this
    }

    PriceDto build() {
        return new PriceDto(value)
    }
}
