package tdd.vendingMachine;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author macko
 * @since 2015-08-19
 */
public class ProductTest {

    private Product productA;
    private Product productA_1;
    private Product productB;

    @Before
    public void setUp() throws Exception {
        productA = new Product(ProductUtils.BANANA_TYPE);
        productA_1 = new Product(ProductUtils.BANANA_TYPE);
        productB = new Product(ProductUtils.BEER_TYPE);
    }

    @Test
    public void differentProductsShouldNotEqual() throws Exception {
        assertNotEquals(productA, productB);
    }

    @Test
    public void sameProductShouldEqual() throws Exception {
        assertEquals(productA, productA_1);
    }
}
