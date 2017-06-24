package tdd.vendingmachine.acceptancetest

import spock.lang.Specification

class SelectingShelfAcceptanceTest extends Specification {

    def "selecting a shelf's number should show the shelf's product's price"() {
        given: "exists Vending Machine with some shelves and products"
        when: "one selects a shelf number"
        then: "the display shows price of product on that shelf"
    }

    def "selecting a non-existing shelf's number should show a warning message"() {
        given: "exists Vending Machine with some shelves and products"
        when: "one selects a non-existing shelf number"
        then: "the display shows a warning message"
    }

    def "selecting a shelf's number with no products on it should show a warning message"() {
        given: "exists Vending Machine with some shelves and products"
        when: "one selects a shelf's number with no products on the shelf"
        then: "the display shows a warning message"
    }
}
