package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.core.ProductName;
import tdd.vendingMachine.core.ProductPrice;
import tdd.vendingMachine.impl.BasicProduct;

import static org.junit.Assert.assertTrue;

public class ProductTest {

    @Test(expected = IllegalArgumentException.class)
    public void product_price_should_not_be_null_empty_or_less_than_0() {
        try {
            ProductPrice.valueOf(null);
        } catch (IllegalArgumentException ignored1) {
            try {
                ProductPrice.valueOf("");
            } catch (IllegalArgumentException ignored2) {
                ProductPrice.valueOf("0");
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void produce_price_should_accept_only_valid_values() {
        ProductPrice.valueOf("abra cadabra");
    }

    @Test(expected = IllegalArgumentException.class)
    public void product_name_should_not_be_null_or_empty() {
        try {
            ProductName.valueOf(null);
        } catch (IllegalArgumentException ignored) {
            ProductName.valueOf("");
        }
    }

    @Test
    public void product_should_have_name_and_price() {
        ProductName productName = ProductName.valueOf("Basic Product");
        ProductPrice productPrice = ProductPrice.valueOf("10");

        BasicProduct product = new BasicProduct(productName, productPrice);
        assertTrue(productName.equals(product.getName()));
        assertTrue(productPrice.equals(product.getPrice()));
    }
}
