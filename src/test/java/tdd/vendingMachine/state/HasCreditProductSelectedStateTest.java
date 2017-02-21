package tdd.vendingMachine.state;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.util.TestUtils.TestUtils;

import java.util.Arrays;
import java.util.Collection;

import static tdd.vendingMachine.util.Constants.ACCURACY;

/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 */
public class HasCreditProductSelectedStateTest {

    VendingMachine hasCreditProductSelectedVendingMachine;
    private Product COLA_199_025;
    private Product CHIPS_025;
    private Product CHOCOLATE_BAR;

    @Before
    public void setup() {
        COLA_199_025 = new Product(1.99, "COLA_199_025");
        CHIPS_025 = new Product(1.29, "CHIPS_025");
        CHOCOLATE_BAR = new Product(1.49, "CHOCOLATE_BAR");
        Collection<Product> products = Arrays.asList(COLA_199_025, CHIPS_025, CHOCOLATE_BAR);
        hasCreditProductSelectedVendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(products, 3), TestUtils.buildCoinDispenserWithGivenItemsPerShelf(20, 5));

        //add credit to machine
        Coin tenCents = Coin.TEN_CENTS;
        hasCreditProductSelectedVendingMachine.insertCoin(tenCents);
        hasCreditProductSelectedVendingMachine.selectShelfNumber(1);

        Assert.assertEquals(tenCents.denomination, hasCreditProductSelectedVendingMachine.getCredit(), ACCURACY);
        Assert.assertNotNull(hasCreditProductSelectedVendingMachine.getSelectedProduct());
        Assert.assertTrue(hasCreditProductSelectedVendingMachine.getCurrentState() instanceof HasCreditProductSelectedState);
    }

    @After
    public void tearDown() {
        COLA_199_025 = null;
        CHIPS_025 = null;
        CHOCOLATE_BAR = null;
        hasCreditProductSelectedVendingMachine = null;
    }

    @Test
    public void should_have_credit_after_insert_coin() {
    }














}
