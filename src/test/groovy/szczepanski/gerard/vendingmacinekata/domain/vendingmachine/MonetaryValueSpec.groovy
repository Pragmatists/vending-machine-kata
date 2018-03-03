package szczepanski.gerard.vendingmacinekata.domain.vendingmachine

import spock.lang.Specification

import static szczepanski.gerard.vendingmacinekata.domain.vendingmachine.Currency.PLN

class MonetaryValueSpec extends Specification {

    def "should throw NPE when value passed on creation is null"() {
        when:
        MonetaryValue.of(null, PLN)

        then:
        thrown NullPointerException
    }

    def "should throw IllegalStateException when value passed on creation is negative"() {
        when:
        MonetaryValue.of(new BigDecimal("-1.00"), PLN)

        then:
        thrown IllegalStateException
    }


    def "should throw NPE when currency passed on creation is null"() {
        when:
        MonetaryValue.of(new BigDecimal("12.00"), null)

        then:
        thrown NullPointerException
    }

    def "should create valid object on static factory mentod creation"() {
        when:
        MonetaryValue value = MonetaryValue.of(new BigDecimal("12.00"), PLN)
        then:
        value
    }

    def "should passed value be set after creation"() {
        given:
        BigDecimal value = new BigDecimal("12.00")

        when:
        MonetaryValue monetaryValue = MonetaryValue.of(value, PLN)

        then:
        monetaryValue.getValue() == value
    }


    def "should passed currency be set after creation"() {
        when:
        MonetaryValue monetaryValue = MonetaryValue.of(new BigDecimal("12.00"), PLN)

        then:
        monetaryValue.getCurrency() == PLN
    }
}
