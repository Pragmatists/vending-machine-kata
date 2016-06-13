package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.core.ProductName;
import tdd.vendingMachine.core.ProductPrice;

public class ProductTest {

    @Test(expected = IllegalArgumentException.class)
    public void price_should_not_be_null() {
        ProductPrice.valueOf(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void price_should_not_be_empty() {
        ProductPrice.valueOf("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void price_should_not_be_zero() {
        ProductPrice.valueOf("0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void price_should_accept_only_valid_values() {
        ProductPrice.valueOf("abra cadabra");
    }

    @Test(expected = IllegalArgumentException.class)
    public void name_should_not_be_null() {
        ProductName.valueOf(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void name_should_not_be_empty() {
        ProductName.valueOf("");
    }
}
