package tdd.vendingmachine.acceptancetest

import spock.lang.Specification

class CancelBuyingProductAcceptanceTest extends Specification {

    def "should return the inserted money after canceling the transaction"() {
        given: "exists Vending Machine containing a shelf with the product"
        and: "one has selected shelf number with that product"
        and: "one has inserted insufficient amount of money to buy the product"
        when: "one presses 'Cancel'"
        then: "the inserted money can be taken as change"
    }
}
