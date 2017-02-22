package tdd.vendingMachine.state;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.util.Constants;
import tdd.vendingMachine.util.TestUtils.TestUtils;

import java.util.Arrays;
import java.util.List;

import static tdd.vendingMachine.util.Constants.ACCURACY;

/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 */
public class HasCreditNoProductSelectedStateTest {

    private Product COLA_199_025;
    private Product CHIPS_025;
    private Product CHOCOLATE_BAR;
    HasCreditNoProductSelectedState hasCreditNoProductSelectedState;

    @Before
    public void setup(){
        COLA_199_025 = new Product(1.99, "COLA_199_025");
        CHIPS_025 = new Product(1.29, "CHIPS_025");
        CHOCOLATE_BAR = new Product(1.49, "CHOCOLATE_BAR");

        VendingMachine vendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(COLA_199_025, 1), TestUtils.buildCoinDispenserWithGivenItemsPerShelf(10, 5));
        Assert.assertEquals(0, vendingMachine.getCredit(), ACCURACY); //no credit
        Assert.assertNull(vendingMachine.getSelectedProduct()); //no product
        Assert.assertTrue(vendingMachine.getCurrentState() instanceof NoCreditNoProductSelectedState);
        NoCreditNoProductSelectedState initialState = (NoCreditNoProductSelectedState) vendingMachine.getCurrentState();

        //transform to desired state
        initialState.insertCoin(Coin.FIFTY_CENTS);

        //validate initial state
        Assert.assertEquals(Coin.FIFTY_CENTS.denomination, initialState.vendingMachine.getCredit(), ACCURACY);
        Assert.assertTrue(initialState.vendingMachine.getCurrentState() instanceof HasCreditNoProductSelectedState);
        hasCreditNoProductSelectedState = (HasCreditNoProductSelectedState) initialState.vendingMachine.getCurrentState();
    }

    @After
    public void tearDown(){
        COLA_199_025 = null;
        CHIPS_025 = null;
        CHOCOLATE_BAR = null;
        hasCreditNoProductSelectedState = null;
    }

    @Test
    public void should_insert_credit_remain_same_state() {
        Coin tenCents = Coin.TEN_CENTS;
        int previousStackCreditSize = hasCreditNoProductSelectedState.vendingMachine.getCreditStackSize();
        double previousCredit = hasCreditNoProductSelectedState.vendingMachine.getCredit();

        hasCreditNoProductSelectedState.insertCoin(tenCents);

        Assert.assertEquals(previousStackCreditSize + 1, hasCreditNoProductSelectedState.vendingMachine.getCreditStackSize());
        Assert.assertEquals(tenCents.denomination + previousCredit, hasCreditNoProductSelectedState.vendingMachine.getCredit(), ACCURACY);
        Assert.assertTrue(hasCreditNoProductSelectedState.vendingMachine.getCurrentState() instanceof HasCreditNoProductSelectedState);
    }

    @Test
    public void should_skip_insert_coins_after_dispensers_capacity_reached() {
        Coin tenCents = Coin.TEN_CENTS;
        int previousStackCreditSize = hasCreditNoProductSelectedState.vendingMachine.getCreditStackSize();
        double previousCredit = hasCreditNoProductSelectedState.vendingMachine.getCredit();
        int insertsToFillCoinShelf = 5;

        //fill ten cents dispenser shelf
        for (int i = 0; i < insertsToFillCoinShelf; i++) {
            hasCreditNoProductSelectedState.insertCoin(tenCents);
        }

        Assert.assertEquals(previousStackCreditSize + insertsToFillCoinShelf, hasCreditNoProductSelectedState.vendingMachine.getCreditStackSize());
        Assert.assertEquals(tenCents.denomination * insertsToFillCoinShelf + previousCredit, hasCreditNoProductSelectedState.vendingMachine.getCredit(), ACCURACY);
        Assert.assertTrue(hasCreditNoProductSelectedState.vendingMachine.getCurrentState() instanceof HasCreditNoProductSelectedState);

        //insert coins will no take any effect
        hasCreditNoProductSelectedState.insertCoin(tenCents);
        hasCreditNoProductSelectedState.insertCoin(tenCents);
        hasCreditNoProductSelectedState.insertCoin(tenCents);
        hasCreditNoProductSelectedState.insertCoin(tenCents);
        hasCreditNoProductSelectedState.insertCoin(tenCents);

        Assert.assertEquals(previousStackCreditSize + insertsToFillCoinShelf, hasCreditNoProductSelectedState.vendingMachine.getCreditStackSize());
        Assert.assertEquals(tenCents.denomination * insertsToFillCoinShelf + previousCredit, hasCreditNoProductSelectedState.vendingMachine.getCredit(), ACCURACY);
        Assert.assertTrue(hasCreditNoProductSelectedState.vendingMachine.getCurrentState() instanceof HasCreditNoProductSelectedState);

    }

    @Test
    public void should_select_valid_shelfNumber_and_change_state_to_HasCreditProductSelectedState() {
        hasCreditNoProductSelectedState.selectShelfNumber(0);
        Assert.assertNotNull(hasCreditNoProductSelectedState.vendingMachine.getSelectedProduct());
        Assert.assertTrue(hasCreditNoProductSelectedState.vendingMachine.getCurrentState() instanceof HasCreditProductSelectedState);
    }

    @Test
    public void should_select_invalid_shelfNumber_and_remain_state_to_HasCreditNoProductSelectedState() {
        hasCreditNoProductSelectedState.selectShelfNumber(5454);
        Assert.assertNull(hasCreditNoProductSelectedState.vendingMachine.getSelectedProduct());
        Assert.assertTrue(hasCreditNoProductSelectedState.vendingMachine.getCurrentState() instanceof HasCreditNoProductSelectedState);
    }

    @Test
    public void should_not_add_credit_return_all_inserted_credit_after_cancel_and_change_to_noCreditState() {
        hasCreditNoProductSelectedState.cancel();

        Assert.assertTrue(hasCreditNoProductSelectedState.vendingMachine.isCreditStackEmpty());
        Assert.assertEquals(0, hasCreditNoProductSelectedState.vendingMachine.getCredit(), ACCURACY);
        Assert.assertTrue(hasCreditNoProductSelectedState.vendingMachine.getCurrentState() instanceof NoCreditNoProductSelectedState);
    }

    @Test
    public void should_add_credit_return_all_inserted_credit_after_cancel_and_change_to_noCreditState() {
        List<Coin> coinsToInsert = Arrays.asList(Coin.FIFTY_CENTS, Coin.TEN_CENTS, Coin.TWENTY_CENTS, Coin.TWO, Coin.ONE, Coin.FIVE);

        int previousStackCreditSize = hasCreditNoProductSelectedState.vendingMachine.getCreditStackSize();
        double previousCredit = hasCreditNoProductSelectedState.vendingMachine.getCredit();

        coinsToInsert.forEach(hasCreditNoProductSelectedState::insertCoin);

        Assert.assertEquals(previousStackCreditSize + coinsToInsert.size(), hasCreditNoProductSelectedState.vendingMachine.getCreditStackSize());
        Double total = coinsToInsert.stream()
            .mapToDouble(coin -> coin.denomination)
            .reduce(Constants.SUM_DOUBLE_IDENTITY, Constants.SUM_DOUBLE_BINARY_OPERATOR);
        Assert.assertEquals(total + previousCredit, hasCreditNoProductSelectedState.vendingMachine.getCredit(), ACCURACY);

        hasCreditNoProductSelectedState.cancel();

        Assert.assertTrue(hasCreditNoProductSelectedState.vendingMachine.isCreditStackEmpty());
        Assert.assertEquals(0, hasCreditNoProductSelectedState.vendingMachine.getCredit(), ACCURACY);
        Assert.assertTrue(hasCreditNoProductSelectedState.vendingMachine.getCurrentState() instanceof NoCreditNoProductSelectedState);
    }
}
