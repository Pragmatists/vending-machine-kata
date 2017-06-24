package tdd.vendingmachine.acceptancetest

import spock.lang.Specification

class BuyingProductAcceptanceTest extends Specification {

    def "inserting enough money should release the product and the change"() {
        given: "exists Vending Machine containing a shelf with the product"
        and: "one has selected shelf number with that product"
        when: "one has inserted enough money to buy the product"
        then: "the product can be taken"
        and: "the change can be taken"
    }

    def "should not be able to buy product if change cannot be given"() {
        given: "exists Vending Machine with little money"
        and: "one has selected shelf number with a product"
        when: "one has inserted enough money to buy the product, but machine will not be able to return the change"
        then: "the display shows a warning message"
        and: "the inserted money can be taken as change"
        and: "the product is not returned and thus cannot be taken"
    }
}
