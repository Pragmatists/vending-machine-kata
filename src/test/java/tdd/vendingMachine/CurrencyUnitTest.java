package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.core.CurrencyUnit;

public class CurrencyUnitTest {

    @Test(expected = IllegalArgumentException.class)
    public void should_not_be_null() {
        CurrencyUnit.valueOf(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_not_be_empty() {
        CurrencyUnit.valueOf("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_not_be_non_number() {
        CurrencyUnit.valueOf("abra cadabra");
    }
}
