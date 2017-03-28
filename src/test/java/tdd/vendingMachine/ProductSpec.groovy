package tdd.vendingMachine

import spock.lang.Specification

/**
 * @author kdkz
 */
class ProductSpec extends Specification{

    def "should return existing product for valid product name"(){
        when:
        Product product = Product.getProductByName("Cola")

        then:
        product == Product.COLA
    }

    def "should return EMPTY value for product which is not existing in enum"() {
        when:
        Product product = Product.getProductByName("notMatch")

        then:
        product == Product.EMPTY
    }
}
