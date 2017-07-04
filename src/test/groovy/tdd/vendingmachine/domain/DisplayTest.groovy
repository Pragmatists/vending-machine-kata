package tdd.vendingmachine.domain

import spock.lang.Specification
import tdd.vendingmachine.testutility.PriceTestBuilder

class DisplayTest extends Specification {
    
    def "should show text as empty String, if display is empty"() {
        given:
            Display display = Display.empty()
        when:
            String text = display.show()
        then:
            text == ""
    }
    
    def "should show 'No shelf', if displaying message for shelf not found"() {
        given:
            ShelfNumber shelfNumber = new ShelfNumber("25")
            Display display = Display.shelfNotFound(shelfNumber)
        when:
            String text = display.show()
        then:
            text == 'No shelf: #25'
    }
    
    def "should show 'Empty shelf', if displaying message for empty shelf"() {
        given:
            ShelfNumber shelfNumber = new ShelfNumber("3")
            Display display = Display.emptyShelf(shelfNumber)
        when:
            String text = display.show()
        then:
            text == 'Empty shelf: #3'
    }
    
    def "should show price in format #.##"() {
        given:
            Price price = Price.create(PriceTestBuilder.aPrice()
                                                       .withValue(priceValue)
                                                       .build())
            Display display = Display.money(price)
        when:
            String text = display.show()
        then:
            text == expectedText
        where:
            priceValue     || expectedText
            1.00           || '1.00'
            1.0            || '1.00'
            BigDecimal.ONE || '1.00'
            2.45           || '2.45'
            100.489        || '100.49'
            4.4124         || '4.41'
            6.054          || '6.05'
            6.055          || '6.06'
            6.056          || '6.06'
    }
}
