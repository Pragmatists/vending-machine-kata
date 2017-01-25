package tdd.vendingMachine;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Created by dzalunin on 2017-01-25.
 */
@RunWith(Parameterized.class)
public class ShelvesTest {


    private int shelve;
    private Product product;
    private int quantity;

    public ShelvesTest(int shelve, Product product, int quantity) {
        this.shelve = shelve;
        this.product = product;
        this.quantity = quantity;
    }

    @Parameterized.Parameters
    public static Object[][] invalidData() {
        return new Object[][]{
                {-1, Product.COCA_COLA, 10},
                {1, null, 10},
                {1, Product.COCA_COLA, -1}
        };
    }

    @Test
    public void testPeekProductSuccess() throws Exception {
        Shelves shelves = new Shelves();
        int quantity = 10;
        int shelveNo = 1;
        shelves.supplyProduct(shelveNo, Product.COCA_COLA, quantity);
        Product product = shelves.peek(shelveNo);
        assertThat(product).isEqualTo(product.COCA_COLA);
        assertThat(shelves.count(shelveNo)).isEqualTo(quantity);
    }

    @Test
    public void testPeekProductFromEmptyShelve() throws Exception {
        Shelves shelves = new Shelves();
        Product product = shelves.peek(1);
        assertThat(product).isNull();
    }

    @Test
    public void testSupplyProductSuccess() throws Exception {
        Shelves shelves = new Shelves();
        int shelveNo = 1;
        shelves.supplyProduct(shelveNo, Product.COCA_COLA, 10);
        shelves.supplyProduct(shelveNo, Product.COCA_COLA, 3);
        long actual = shelves.count(shelveNo);
        assertThat(actual).isEqualTo(13);
    }

    @Test(expected = ShelveAlreadyIsBusy.class)
    public void testSupplyOnBusyShelveException() throws Exception {
        Shelves shelves = new Shelves();
        shelves.supplyProduct(1, Product.COCA_COLA, 10);
        shelves.supplyProduct(1, Product.KROPLA_BESKIDU, 10);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testSupplyIllegalArgument() throws Exception {
        Shelves shelves = new Shelves();
        shelves.supplyProduct(shelve, product, quantity);
    }

    @Test
    public void testTakeProductSuccess() throws Exception {
        Shelves shelves = new Shelves();
        int shelveNo = 1;
        shelves.supplyProduct(shelveNo, Product.COCA_COLA, 10);
        Product actual = shelves.take(shelveNo);
        assertThat(actual).isEqualTo(Product.COCA_COLA);
        assertThat(shelves.count(shelveNo)).isEqualTo(9);
    }

    @Test
    public void testTakeProductFromEmptyShelve() throws Exception {
        Shelves shelves = new Shelves();
        Product actual = shelves.take(1);
        assertThat(actual).isNull();
    }
}