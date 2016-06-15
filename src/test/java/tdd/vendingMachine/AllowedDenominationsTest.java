package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.core.CurrencyUnit;
import tdd.vendingMachine.impl.AllowedDenominations;

import static org.assertj.core.api.Assertions.assertThat;

public class AllowedDenominationsTest {

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_exception_when_constructed_from_null_currency() {
        new AllowedDenominations().add(null);
    }

    @Test
    public void check_allowed_denomination() {
        assertThat(new AllowedDenominations()
            .add(CurrencyUnit.valueOf("1"))
            .isAllowed(CurrencyUnit.valueOf("1"))
        ).isEqualTo(true);
    }
}
