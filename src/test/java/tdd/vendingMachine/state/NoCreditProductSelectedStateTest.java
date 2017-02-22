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
public class NoCreditProductSelectedStateTest {

    private Product COLA_199_025;
    private Product CHIPS_025;
    private Product CHOCOLATE_BAR;
    private NoCreditProductSelectedState noCreditProductSelectedState;

    @Before
    public void setup() {
        COLA_199_025 = new Product(1.99, "COLA_199_025");
        CHIPS_025 = new Product(1.29, "CHIPS_025");
        CHOCOLATE_BAR = new Product(1.49, "CHOCOLATE_BAR");
        Collection<Product> ts = Arrays.asList(COLA_199_025, CHIPS_025, CHOCOLATE_BAR);
        VendingMachine vendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(ts, 3), TestUtils.buildCoinDispenserWithGivenItemsPerShelf(20, 5));
        Assert.assertEquals(0, vendingMachine.getCredit(), ACCURACY); //no credit
        Assert.assertNull(vendingMachine.getSelectedProduct()); //no product
        Assert.assertTrue(vendingMachine.getCurrentState() instanceof NoCreditNoProductSelectedState);
        NoCreditNoProductSelectedState initialState = (NoCreditNoProductSelectedState) vendingMachine.getCurrentState();

        //transform to get desired state
        initialState.selectShelfNumber(0);

        //validate initial state
        Assert.assertNotNull(initialState.vendingMachine.getSelectedProduct());
        Assert.assertTrue(initialState.vendingMachine.getCurrentState() instanceof NoCreditProductSelectedState);
        noCreditProductSelectedState = (NoCreditProductSelectedState) initialState.vendingMachine.getCurrentState();
    }

    @After
    public void tearDown() {
        COLA_199_025 = null;
        CHIPS_025 = null;
        CHOCOLATE_BAR = null;
        noCreditProductSelectedState = null;
    }

    @Test
    public void should_have_credit_after_insert_coin() {
        Coin tenCents = Coin.TEN_CENTS;

        noCreditProductSelectedState.insertCoin(tenCents);

        Assert.assertEquals(tenCents.denomination, noCreditProductSelectedState.vendingMachine.getCredit(), ACCURACY);
        Assert.assertEquals(1, noCreditProductSelectedState.vendingMachine.getCreditStackSize());
        Assert.assertNull(noCreditProductSelectedState.vendingMachine.getSelectedProduct());
        Assert.assertTrue(noCreditProductSelectedState.vendingMachine.getCurrentState() instanceof HasCreditNoProductSelectedState);
    }

    @Test
    public void should_have_selected_product_after_selecting_valid_shelfNumber() {

        noCreditProductSelectedState.selectShelfNumber(0);

        Assert.assertEquals(0, noCreditProductSelectedState.vendingMachine.getCreditStackSize());
        Assert.assertNotNull(noCreditProductSelectedState.vendingMachine.getSelectedProduct());
        Assert.assertTrue(noCreditProductSelectedState.vendingMachine.getCurrentState() instanceof NoCreditProductSelectedState);
    }

    @Test
    public void should_not_have_selected_product_after_selecting_invalid_shelfNumber() {

        noCreditProductSelectedState.selectShelfNumber(582);

        Assert.assertEquals(0, noCreditProductSelectedState.vendingMachine.getCreditStackSize());
        Assert.assertNull(noCreditProductSelectedState.vendingMachine.getSelectedProduct());
        Assert.assertTrue(noCreditProductSelectedState.vendingMachine.getCurrentState() instanceof NoCreditNoProductSelectedState);
    }
}
