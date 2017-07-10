package tdd.vendingmachine.testutility

import spock.lang.Specification

class RandomizerTest extends Specification{
    
    def "should return always a different shelf number"() {
        given:
            String shelfNumber = Randomizer.aShelfNumber()
        when:
            String differentShelfNumber = Randomizer.aDifferentShelfNumber(shelfNumber)
        then:
            shelfNumber != differentShelfNumber
        where:
            repeat100Times << (1..100)
    }
}
