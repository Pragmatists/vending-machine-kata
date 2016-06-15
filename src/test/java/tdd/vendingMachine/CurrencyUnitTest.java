package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.core.CurrencyUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void should_return_value() {
        assertThat(CurrencyUnit.valueOf("10").value()).isNotEmpty();
    }

    @Test
    public void should_be_zero() {
        assertTrue(CurrencyUnit.zero().isZero());
    }

    @Test
    public void should_be_comparable() {
        assertThat(CurrencyUnit.zero()).isEqualByComparingTo(CurrencyUnit.zero());
    }

    @Test
    public void should_equal() {
        assertThat(CurrencyUnit.zero()).isEqualTo(CurrencyUnit.zero());
    }

    @Test
    public void could_be_integer() {
        assertThat(CurrencyUnit.zero().toInteger()).isEqualTo(0);
    }

    @Test
    public void should_check_positive() throws Exception {
        assertTrue(CurrencyUnit.valueOf("1").isPositive());
    }

    @Test
    public void should_check_negative() throws Exception {
        assertTrue(CurrencyUnit.valueOf("-1").isNegative());
    }

    @Test
    public void should_check_zero() throws Exception {
        assertTrue(CurrencyUnit.zero().isZero());
    }

    @Test
    public void should_check_greater_than() throws Exception {
        assertTrue(CurrencyUnit.valueOf("1").greaterThan(CurrencyUnit.zero()));
    }

    @Test
    public void sould_check_greater_or_equal_than() throws Exception {
        assertTrue(CurrencyUnit.zero().greaterOrEqualThan(CurrencyUnit.zero()));
    }

    @Test
    public void should_add() throws Exception {
        assertThat(CurrencyUnit.zero().add(CurrencyUnit.valueOf("1")))
            .isEqualTo(CurrencyUnit.valueOf("1"));
    }

    @Test
    public void should_subtract() throws Exception {
        assertThat(CurrencyUnit.zero().subtract(CurrencyUnit.valueOf("1")))
            .isEqualTo(CurrencyUnit.valueOf("-1"));
    }

    @Test
    public void should_multiply_by_integer() throws Exception {
        assertThat(CurrencyUnit.valueOf("4").multiply(4))
            .isEqualTo(CurrencyUnit.valueOf("16"));
    }

    @Test
    public void should_divide() throws Exception {
        assertThat(CurrencyUnit.valueOf("16").divide(CurrencyUnit.valueOf("4")))
            .isEqualTo(CurrencyUnit.valueOf("4"));
    }
}
