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

    @Test(expected = ManyProductsOnOneShelfException.class)
    public void onlyOneProductTypeCanBePlacedOnShelf() throws Exception {
        Product productA = new Product(ProductUtils.BANANA_TYPE);
        Product productB = new Product(ProductUtils.BEER_TYPE);

        machine.addProductToShelf(1, productA);
        machine.addProductToShelf(1, productB);
    }

    @Test
    public void fewProductsOfSameTypeCanBePlacedOnOneShelf() throws Exception {
        Product productA_0 = new Product(ProductUtils.BANANA_TYPE);
        Product productA_1 = new Product(ProductUtils.BANANA_TYPE);

        machine.addProductToShelf(1, productA_0);
        machine.addProductToShelf(1, productA_1);
    }
}
