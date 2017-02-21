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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static tdd.vendingMachine.util.Constants.ACCURACY;

/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 */
public class NoCreditNoProductSelectedStateTest {


    private Product COLA_199_025;
    private Product CHIPS_025;
    private Product CHOCOLATE_BAR;
    VendingMachine noCreditVendingMachine;

    @Before
    public void setup(){
        COLA_199_025 = new Product(1.99, "COLA_199_025");
        CHIPS_025 = new Product(1.29, "CHIPS_025");
        CHOCOLATE_BAR = new Product(1.49, "CHOCOLATE_BAR");
        noCreditVendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(COLA_199_025, 1), TestUtils.buildCoinDispenserWithGivenItemsPerShelf(10, 5));
        Assert.assertNull(noCreditVendingMachine.getSelectedProduct());
        Assert.assertTrue(noCreditVendingMachine.getCurrentState() instanceof NoCreditNoProductSelectedState);
    }

    @After
    public void tearDown(){
        COLA_199_025 = null;
        CHIPS_025 = null;
        CHOCOLATE_BAR = null;
        noCreditVendingMachine = null;
    }

    @Test
    public void should_return_all_inserted_credit_after_cancel_and_change_to_noCreditState() {
        List<Coin> coinsToInsert = Arrays.asList(Coin.FIFTY_CENTS, Coin.TEN_CENTS, Coin.TWENTY_CENTS, Coin.TWO, Coin.ONE, Coin.FIVE);

        Assert.assertEquals(0, noCreditVendingMachine.getCredit(), ACCURACY);

        coinsToInsert.forEach(noCreditVendingMachine::insertCoin);

        Assert.assertEquals(6, noCreditVendingMachine.getCreditStack().size());
        Optional<Double> total = coinsToInsert.stream()
            .map(coin -> coin.denomination)
            .reduce((a, b) -> a + b);
        Assert.assertEquals(total.isPresent()? total.get() : 0, noCreditVendingMachine.getCredit(), ACCURACY);

        noCreditVendingMachine.cancel();

        Assert.assertTrue(noCreditVendingMachine.getCreditStack().isEmpty());
        Assert.assertEquals(0, noCreditVendingMachine.getCredit(), ACCURACY);
        Assert.assertTrue(noCreditVendingMachine.getCurrentState() instanceof NoCreditNoProductSelectedState);
    }
}
