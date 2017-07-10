package tdd.vendingmachine.domain

import spock.lang.Specification
import tdd.vendingmachine.domain.dto.PriceDto

class PriceTest extends Specification {
    
    def "should Price be equal to another Price if scales of their values are different"() {
        given:
            BigDecimal valueOfScale1 = 1.0
            BigDecimal valueOfScale2 = 1.00
        and:
            PriceDto priceDto1 = new PriceDto(valueOfScale1)
            PriceDto priceDto2 = new PriceDto(valueOfScale2)
        and:
            Price price1 = Price.create(priceDto1)
            Price price2 = Price.create(priceDto2)
        expect:
            !(valueOfScale1.equals(valueOfScale2))
        when:
            boolean equal = (price1.equals(price2))
        then:
            equal
    }
}
