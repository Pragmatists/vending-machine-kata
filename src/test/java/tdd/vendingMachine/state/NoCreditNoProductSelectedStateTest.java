package tdd.vendingMachine.state;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.util.TestUtils.TestUtils;

import java.util.Collections;

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
    private int coinShelfCapacity;
    private int initialCoinsOnShelf;

    private void validInitialState(VendingMachine vendingMachine) {
        Assert.assertEquals(0, vendingMachine.getCredit(), ACCURACY); //no credit
        Assert.assertNull(vendingMachine.getSelectedProduct()); //no product
        Assert.assertTrue(vendingMachine.getCurrentState() instanceof NoCreditNoProductSelectedState);
    }

    @Before
    public void setup(){
        COLA_199_025 = new Product(1.99, "COLA_199_025");
        CHIPS_025 = new Product(1.29, "CHIPS_025");
        CHOCOLATE_BAR = new Product(1.49, "CHOCOLATE_BAR");
        coinShelfCapacity = 10;
        initialCoinsOnShelf = 5;
        VendingMachine vendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(COLA_199_025, 1), TestUtils.buildCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, initialCoinsOnShelf));
        validInitialState(vendingMachine);
        noCreditNoProductSelectedState = (NoCreditNoProductSelectedState) vendingMachine.getCurrentState();
    }

    @After
    public void tearDown(){
        COLA_199_025 = null;
        CHIPS_025 = null;
        CHOCOLATE_BAR = null;
        noCreditNoProductSelectedState = null;
        coinShelfCapacity = 0;
        initialCoinsOnShelf = 0;
    }

    @Test
    public void should_change_state_after_inserting_coin_on_machine_with_dispenser_available() {
        Coin tenCents = Coin.TEN_CENTS;

        noCreditNoProductSelectedState.insertCoin(tenCents);

        Assert.assertEquals(tenCents.denomination, noCreditNoProductSelectedState.vendingMachine.getCredit(), ACCURACY);
        Assert.assertTrue(noCreditNoProductSelectedState.vendingMachine.getCurrentState() instanceof HasCreditNoProductSelectedState);
    }

    @Test
    public void should_not_change_state_and_skip_coin_insertions_when_coin_dispenser_full() {
        Coin tenCents = Coin.TEN_CENTS;
        VendingMachine vendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(CHOCOLATE_BAR, 1),
            TestUtils.buildCoinDispenserWithGivenItemsPerShelf(coinShelfCapacity, coinShelfCapacity));
        validInitialState(vendingMachine);
        noCreditNoProductSelectedState = (NoCreditNoProductSelectedState) vendingMachine.getCurrentState();

        noCreditNoProductSelectedState.insertCoin(tenCents);

        Assert.assertEquals(0, noCreditNoProductSelectedState.vendingMachine.getCredit(), ACCURACY);
        Assert.assertEquals(0, noCreditNoProductSelectedState.vendingMachine.getCreditStackSize());
        Assert.assertTrue(noCreditNoProductSelectedState.vendingMachine.getCurrentState() instanceof NoCreditNoProductSelectedState);
    }

    @Test
    public void should_change_state_after_selecting_valid_shelfNumber() {
        noCreditNoProductSelectedState.selectShelfNumber(0);
        Assert.assertNotNull(noCreditNoProductSelectedState.vendingMachine.getSelectedProduct());
        Assert.assertTrue(noCreditNoProductSelectedState.vendingMachine.getCurrentState() instanceof NoCreditProductSelectedState);
    }

    @Test
    public void should_not_change_state_after_selecting_invalid_shelfNumber() {
        noCreditNoProductSelectedState.selectShelfNumber(5515);
        validInitialState(noCreditNoProductSelectedState.vendingMachine);
    }

    @Test
    public void should_not_change_state_after_cancel() {
        noCreditNoProductSelectedState.cancel();
        validInitialState(noCreditNoProductSelectedState.vendingMachine);
    }
}
