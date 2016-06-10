package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.core.IllegalProductPriceException;
import tdd.vendingMachine.core.ProductPrice;

public class ProductTest {

    @Test(expected = IllegalProductPriceException.class)
    public void product_price_should_not_be_null_empty_or_less_than_0() {
        try {
            ProductPrice.valueOf(null);
        } catch (IllegalProductPriceException ignored1) {
            try {
                ProductPrice.valueOf("");
            } catch (IllegalProductPriceException ignored2) {
                ProductPrice.valueOf("0");
            }
        }
    }

    @Test(expected = NumberFormatException.class)
    public void produce_price_should_accept_only_valid_values() {
        ProductPrice.valueOf("abra cadabra");
    }
}
