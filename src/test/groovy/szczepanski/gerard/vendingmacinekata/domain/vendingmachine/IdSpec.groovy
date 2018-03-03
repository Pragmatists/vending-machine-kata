package szczepanski.gerard.vendingmacinekata.domain.vendingmachine

import spock.lang.Specification

class IdSpec extends Specification {

    def "should create Id object on generate"() {
        when:
        Id id = Id.generate()

        then:
        id
    }

    def "should created Id has unique String value without dashes"() {
        when:
        Id id = Id.generate()

        then:
        !id.plainValue().contains("-")
    }
}
