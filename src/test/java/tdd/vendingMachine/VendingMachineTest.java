package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.core.CurrencyUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class VendingMachineTest {

    @Test
    public void vending_machine_should_configure_allowed_denominations() {
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.addAllowedDenomination(CurrencyUnit.valueOf("2"));
        vendingMachine.addAllowedDenomination(CurrencyUnit.valueOf("5"));

        assertThat(vendingMachine.getAllowedDenominations().size()).isEqualTo(2);
    }
}
