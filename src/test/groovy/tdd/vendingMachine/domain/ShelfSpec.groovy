package tdd.vendingMachine.domain

import spock.lang.Specification
import tdd.vendingMachine.exception.VendingException

/**
 * @author kdkz
 */
class ShelfSpec extends Specification {

    Shelf shelf

    def setup() {
        shelf = new Shelf()
    }

    def "putProductsOnShelf should set products on shelf with positive value"() {
        when:
        shelf.putProductsOnShelf(product, quantity)

        then:
        shelf.productsOnShelf.key == expectedProduct
        shelf.productsOnShelf.value == expectedQuantity

        where:
        product      | expectedProduct | quantity | expectedQuantity
        Product.COLA | Product.COLA    | 1        | 1
        Product.COLA | Product.COLA    | 9        | 8
        "Cola"       | Product.COLA    | 1        | 1
    }

    def "putProductsOnShelf should throw an Exception for particular arguments"() {
        when:
        shelf.putProductsOnShelf(product, quantity)

        then:
        thrown(VendingException)

        where:
        product         |   quantity
        "notMatch"      |   1
        Product.COLA    |   0
        Product.COLA    |   -1
    }

    def "getCurrentProductCount should return current product count on the shelf"(){
        given:
        shelf.putProductsOnShelf(Product.COLA, 4)

        when:
        def productCount = shelf.getCurrentProductCount()

        then:
        productCount == 4
    }

    def "takeOffProduct should return valid decreased count of product on shelf"() {
        given:
        shelf.putProductsOnShelf(Product.COLA, 4)

        when:
        def currentCount = shelf.takeOffProduct()

        then:
        currentCount == 3
    }

    def "should throw an Exception is current count is lower or equal 0"() {
        when:
        shelf.takeOffProduct()

        then:
        thrown(VendingException)
    }
}
