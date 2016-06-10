package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.core.ProductName;
import tdd.vendingMachine.core.ProductPrice;
import tdd.vendingMachine.impl.BasicProduct;
import tdd.vendingMachine.impl.BasicShelve;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ShelveTest {

    @Test
    public void shelve_should_contain_products() {
        BasicShelve shelve = new BasicShelve();

        shelve.put(new BasicProduct(ProductName.valueOf("Product 1"), ProductPrice.valueOf("1")))
            .put(new BasicProduct(ProductName.valueOf("Product 2"), ProductPrice.valueOf("2")));

        assertTrue(shelve.hasProducts());

        BasicProduct product = shelve.withdraw();

        assertThat(product.getName()).isEqualTo("Product 1");
        assertThat(product.getPrice()).isEqualTo("1.0");

        product = shelve.withdraw();

        assertThat(product.getName()).isEqualTo("Product 2");
        assertThat(product.getPrice()).isEqualTo("2.0");

        assertFalse(shelve.hasProducts());
    }
}
