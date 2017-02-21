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
import java.util.List;
import java.util.Optional;

import static tdd.vendingMachine.util.Constants.ACCURACY;

/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 */
public class HasCreditNoProductSelectedStateTest {

    private Product COLA_199_025;
    private Product CHIPS_025;
    private Product CHOCOLATE_BAR;
    VendingMachine hasCreditNoProductSelectedVendingMachine;

    @Before
    public void setup(){
        COLA_199_025 = new Product(1.99, "COLA_199_025");
        CHIPS_025 = new Product(1.29, "CHIPS_025");
        CHOCOLATE_BAR = new Product(1.49, "CHOCOLATE_BAR");
        hasCreditNoProductSelectedVendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(COLA_199_025, 1), TestUtils.buildCoinDispenserWithGivenItemsPerShelf(10, 5));

        //validate initial state
        Assert.assertNull(hasCreditNoProductSelectedVendingMachine.getSelectedProduct());
        hasCreditNoProductSelectedVendingMachine.insertCoin(Coin.FIFTY_CENTS);
        Assert.assertEquals(Coin.FIFTY_CENTS.denomination, hasCreditNoProductSelectedVendingMachine.getCredit(), ACCURACY);
        Assert.assertTrue(hasCreditNoProductSelectedVendingMachine.getCurrentState() instanceof HasCreditNoProductSelectedState);
    }

    @After
    public void tearDown(){
        COLA_199_025 = null;
        CHIPS_025 = null;
        CHOCOLATE_BAR = null;
        hasCreditNoProductSelectedVendingMachine = null;
    }

    @Test
    public void should_not_add_credit_return_all_inserted_credit_after_cancel_and_change_to_noCreditState() {
        hasCreditNoProductSelectedVendingMachine.cancel();

        Assert.assertTrue(hasCreditNoProductSelectedVendingMachine.getCreditStack().isEmpty());
        Assert.assertEquals(0, hasCreditNoProductSelectedVendingMachine.getCredit(), ACCURACY);
        Assert.assertTrue(hasCreditNoProductSelectedVendingMachine.getCurrentState() instanceof NoCreditNoProductSelectedState);
    }

    @Test
    public void should_insert_credit_remain_same_state() {
        Coin tenCents = Coin.TEN_CENTS;
        int previousStackCreditSize = hasCreditNoProductSelectedVendingMachine.getCreditStack().size();
        double previousCredit = hasCreditNoProductSelectedVendingMachine.getCredit();

        hasCreditNoProductSelectedVendingMachine.insertCoin(tenCents);

        Assert.assertEquals(previousStackCreditSize + 1, hasCreditNoProductSelectedVendingMachine.getCreditStack().size());
        Assert.assertEquals(tenCents.denomination + previousCredit, hasCreditNoProductSelectedVendingMachine.getCredit(), ACCURACY);
        Assert.assertTrue(hasCreditNoProductSelectedVendingMachine.getCurrentState() instanceof HasCreditNoProductSelectedState);
    }

    @Test
    public void should_add_credit_return_all_inserted_credit_after_cancel_and_change_to_noCreditState() {
        List<Coin> coinsToInsert = Arrays.asList(Coin.FIFTY_CENTS, Coin.TEN_CENTS, Coin.TWENTY_CENTS, Coin.TWO, Coin.ONE, Coin.FIVE);

        int previousStackCreditSize = hasCreditNoProductSelectedVendingMachine.getCreditStack().size();
        double previousCredit = hasCreditNoProductSelectedVendingMachine.getCredit();

        coinsToInsert.forEach(hasCreditNoProductSelectedVendingMachine::insertCoin);

        Assert.assertEquals(previousStackCreditSize + coinsToInsert.size(), hasCreditNoProductSelectedVendingMachine.getCreditStack().size());
        Double total = coinsToInsert.stream()
            .map(coin -> coin.denomination)
            .reduce((a, b) -> a + b).orElse(0.0);
        Assert.assertEquals(total + previousCredit, hasCreditNoProductSelectedVendingMachine.getCredit(), ACCURACY);

        hasCreditNoProductSelectedVendingMachine.cancel();

        Assert.assertTrue(hasCreditNoProductSelectedVendingMachine.getCreditStack().isEmpty());
        Assert.assertEquals(0, hasCreditNoProductSelectedVendingMachine.getCredit(), ACCURACY);
        Assert.assertTrue(hasCreditNoProductSelectedVendingMachine.getCurrentState() instanceof NoCreditNoProductSelectedState);
    }
}
