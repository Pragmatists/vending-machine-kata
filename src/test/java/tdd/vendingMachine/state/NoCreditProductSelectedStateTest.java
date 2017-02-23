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
public class NoCreditProductSelectedStateTest implements StateTest {

    private Product COLA_199_025;
    private Product CHIPS_025;
    private Product CHOCOLATE_BAR;
    private NoCreditProductSelectedState noCreditProductSelectedState;

    @Override
    public NoCreditProductSelectedState transformToInitialState(VendingMachine vendingMachine) {
        Assert.assertEquals(0, vendingMachine.getCredit(), ACCURACY); //no credit
        Assert.assertNull(vendingMachine.getSelectedProduct()); //no product
        Assert.assertTrue(vendingMachine.getCurrentState() instanceof NoCreditNoProductSelectedState);
        NoCreditNoProductSelectedState initialState = (NoCreditNoProductSelectedState) vendingMachine.getCurrentState();

        //transform to get desired state
        initialState.selectShelfNumber(0);

        //validate initial state
        Assert.assertNotNull(initialState.vendingMachine.getSelectedProduct());
        Assert.assertTrue(initialState.vendingMachine.getCurrentState() instanceof NoCreditProductSelectedState);
        return (NoCreditProductSelectedState) initialState.vendingMachine.getCurrentState();
    }

    @Before
    public void setup() {
        COLA_199_025 = new Product(1.99, "COLA_199_025");
        CHIPS_025 = new Product(1.29, "CHIPS_025");
        CHOCOLATE_BAR = new Product(1.49, "CHOCOLATE_BAR");
        Collection<Product> ts = Arrays.asList(COLA_199_025, CHIPS_025, CHOCOLATE_BAR);
        VendingMachine vendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(ts, 3), TestUtils.buildCoinDispenserWithGivenItemsPerShelf(20, 5));
        noCreditProductSelectedState = transformToInitialState(vendingMachine);
    }

    @After
    public void tearDown() {
        COLA_199_025 = null;
        CHIPS_025 = null;
        CHOCOLATE_BAR = null;
        noCreditProductSelectedState = null;
    }
}
