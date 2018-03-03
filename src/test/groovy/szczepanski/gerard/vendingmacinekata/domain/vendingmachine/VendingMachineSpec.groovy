package szczepanski.gerard.vendingmacinekata.domain.vendingmachine

import spock.lang.Specification

class VendingMachineSpec extends Specification {

    def "should be not null after creation"() {
        when:
        VendingMachine machine = new VendingMachine()

        then:
        machine
    }
}
