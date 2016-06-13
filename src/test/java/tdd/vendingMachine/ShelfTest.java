package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.core.Product;
import tdd.vendingMachine.core.ProductName;
import tdd.vendingMachine.core.ProductPrice;
import tdd.vendingMachine.impl.BasicShelf;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ShelfTest {

    @Test
    public void shelve_should_contain_products() {
        BasicShelf shelve = new BasicShelf(ProductName.valueOf("Product 1"), ProductPrice.valueOf("1"));
        shelve.charge(2);

        assertTrue(shelve.hasProducts());

        Product product = shelve.withdraw();

        assertThat(product.getName()).isEqualTo(ProductName.valueOf("Product 1"));
        assertThat(product.getPrice()).isEqualTo(ProductPrice.valueOf("1"));

        product = shelve.withdraw();

        assertThat(product.getName()).isEqualTo(ProductName.valueOf("Product 1"));
        assertThat(product.getPrice()).isEqualTo(ProductPrice.valueOf("1"));

        assertFalse(shelve.hasProducts());
    }
}
