package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.core.*;
import tdd.vendingMachine.impl.BasicProduct;

import static org.junit.Assert.assertTrue;

public class ProductTest {

    @Test(expected = IllegalCurrencyValueException.class)
    public void product_price_should_not_be_null_empty_or_less_than_0() {
        try {
            ProductPrice.valueOf(null);
        } catch (IllegalCurrencyValueException ignored1) {
            try {
                ProductPrice.valueOf("");
            } catch (IllegalCurrencyValueException ignored2) {
                ProductPrice.valueOf("0");
            }
        }
    }

    @Test(expected = IllegalCurrencyValueException.class)
    public void produce_price_should_accept_only_valid_values() {
        ProductPrice.valueOf("abra cadabra");
    }

    @Test(expected = IllegalProductNameException.class)
    public void product_name_should_not_be_null_or_empty() {
        try {
            ProductName.valueOf(null);
        } catch (IllegalProductNameException ignored) {
            ProductName.valueOf("");
        }
    }

    @Test
    public void product_should_have_name_and_price() {
        String productName = "Basic Product";
        String productPrice = "10.0";

        BasicProduct product = new BasicProduct(ProductName.valueOf(productName), ProductPrice.valueOf(productPrice));
        assertTrue(productName.equals(product.getName()));
        assertTrue(productPrice.equals(product.getPrice()));
    }
}
