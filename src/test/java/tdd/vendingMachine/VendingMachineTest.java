package tdd.vendingMachine;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public class VendingMachineTest {

    private VendingMachine machine;

    @Before
    public void setUp() throws Exception {
        machine = new VendingMachine();
    }

    @Test
    public void productsCouldBePlacedOnShelves() throws Exception {
        Product product = new Product(ProductUtils.BANANA_TYPE);
        machine.addProductToShelf(1, product);

        assertSame(product, machine.getProductFromShelf(1));
    }
}
