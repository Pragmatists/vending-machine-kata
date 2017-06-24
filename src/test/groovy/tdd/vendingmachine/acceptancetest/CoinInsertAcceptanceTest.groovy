package tdd.vendingmachine.acceptancetest

import spock.lang.Specification

class CoinInsertAcceptanceTest extends Specification {

    def "inserting a coin should show the amount left to buy a product"() {
        given: "exists Vending Machine containing a shelf with the product"
        and: "one has selected shelf number with a product"
        when: "one inserts a coin"
        then: "the display shows amount that must be added to cover the product's price"
    }

    def "inserting multiple coins should show the reducing amount left to buy a product"() {
        given: "exists Vending Machine containing a shelf with the product"
        and: "one has selected shelf number with a product"
        when: "one inserts a coin"
        then: "the display shows amount that must be added to cover the product's price"
        when: "one inserts another coin"
        then: "the display shows the reduced amount that must be added to cover the product's price"
    }

    def "inserting a coin that is not acceptable, should show a warning message and return the coin as change"() {
        given: "exists Vending Machine with a set of acceptable denominations"
        and: "one has selected shelf number with a product"
        when: "one inserts a non-acceptable coin"
        then: "the display shows a warning message"
        and: "the inserted money can be taken as change"
    }
}
