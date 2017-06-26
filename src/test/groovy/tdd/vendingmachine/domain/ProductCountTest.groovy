package tdd.vendingmachine.domain

import spock.lang.Specification

class ProductCountTest extends Specification {
    
    def "should Product Count be only positive"() {
        given:
            int count = -1
        when:
            new ProductCount(count)
        then:
            thrown(IllegalArgumentException)
    }
    
}
