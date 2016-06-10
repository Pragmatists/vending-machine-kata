package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.core.CurrencyUnit;
import tdd.vendingMachine.core.IllegalCurrencyValueException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CurrencyUnitTest {

    @Test(expected = IllegalCurrencyValueException.class)
    public void currency_unit_should_not_be_null() {
        CurrencyUnit.valueOf(null);
    }

    @Test(expected = IllegalCurrencyValueException.class)
    public void currency_unit_should_not_be_empty() {
        CurrencyUnit.valueOf("");
    }

    @Test(expected = IllegalCurrencyValueException.class)
    public void currency_unit_should_not_be_non_number() {
        CurrencyUnit.valueOf("abra cadabra");
    }

    @Test
    public void currency_unit_should_store_value() {
        assertThat(CurrencyUnit.valueOf("10").value()).isEqualTo("10.0");
    }

    @Test
    public void currency_unit_should_detect_negative_value() {
        assertTrue(CurrencyUnit.valueOf("-10").isNegative());
        assertFalse(CurrencyUnit.valueOf("0").isNegative());
        assertFalse(CurrencyUnit.valueOf("10").isNegative());
    }

    @Test
    public void currency_unit_should_detect_zero_value() {
        assertTrue(CurrencyUnit.valueOf("0").isZero());
        assertFalse(CurrencyUnit.valueOf("2").isZero());
        assertFalse(CurrencyUnit.valueOf("-2").isZero());
    }
}
