package tdd.vendingMachine.state;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.util.TestUtils.TestUtils;

import static tdd.vendingMachine.util.Constants.ACCURACY;

/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 */
public class NoCreditNoProductSelectedStateTest {


    private Product COLA_199_025;
    private Product CHIPS_025;
    private Product CHOCOLATE_BAR;
    private NoCreditNoProductSelectedState noCreditNoProductSelectedState;


    @Before
    public void setup(){
        COLA_199_025 = new Product(1.99, "COLA_199_025");
        CHIPS_025 = new Product(1.29, "CHIPS_025");
        CHOCOLATE_BAR = new Product(1.49, "CHOCOLATE_BAR");
        VendingMachine vendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(COLA_199_025, 1), TestUtils.buildCoinDispenserWithGivenItemsPerShelf(10, 5));
        Assert.assertEquals(0, vendingMachine.getCredit(), ACCURACY); //no credit
        Assert.assertNull(vendingMachine.getSelectedProduct()); //no product
        Assert.assertTrue(vendingMachine.getCurrentState() instanceof NoCreditNoProductSelectedState);
        noCreditNoProductSelectedState = (NoCreditNoProductSelectedState) vendingMachine.getCurrentState();
    }

    @After
    public void tearDown(){
        COLA_199_025 = null;
        CHIPS_025 = null;
        CHOCOLATE_BAR = null;
        noCreditNoProductSelectedState = null;
    }

    @Test
    public void should_change_state_after_inserting_coin_on_machine_with_dispenser_available() {
        Assert.assertFalse(true);
    }

    @Test
    public void should_not_change_state_and_skip_coin_insertions_when_coin_dispenser_full() {
        Assert.assertFalse(true);
    }

    @Test
    public void should_change_state_after_selecting_valid_shelfNumber() {
        Assert.assertFalse(true);
    }

    @Test
    public void should_not_change_state_after_selecting_invalid_shelfNumber() {
        Assert.assertFalse(true);
    }

    @Test
    public void should_not_change_state_after_cancel() {
        Assert.assertFalse(true);
    }
}
