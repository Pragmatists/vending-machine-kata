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
    HasCreditNoProductSelectedState hasCreditNoProductSelected;

    @Before
    public void setup(){
        COLA_199_025 = new Product(1.99, "COLA_199_025");
        CHIPS_025 = new Product(1.29, "CHIPS_025");
        CHOCOLATE_BAR = new Product(1.49, "CHOCOLATE_BAR");
        VendingMachine vendingMachine = new VendingMachine(TestUtils.buildShelvesWithItems(COLA_199_025, 1), TestUtils.buildCoinDispenserWithGivenItemsPerShelf(10, 5));

        //validate initial state
        Assert.assertNull(vendingMachine.getSelectedProduct());
        vendingMachine.insertCoin(Coin.FIFTY_CENTS);
        Assert.assertEquals(Coin.FIFTY_CENTS.denomination, vendingMachine.getCredit(), ACCURACY);
        Assert.assertTrue(vendingMachine.getCurrentState() instanceof HasCreditNoProductSelectedState);
        hasCreditNoProductSelected = (HasCreditNoProductSelectedState) vendingMachine.getCurrentState();
    }

    @After
    public void tearDown(){
        COLA_199_025 = null;
        CHIPS_025 = null;
        CHOCOLATE_BAR = null;
        hasCreditNoProductSelected = null;
    }

    @Test
    public void should_insert_credit_remain_same_state() {
        Coin tenCents = Coin.TEN_CENTS;
        int previousStackCreditSize = hasCreditNoProductSelected.vendingMachine.getCreditStackSize();
        double previousCredit = hasCreditNoProductSelected.vendingMachine.getCredit();

        hasCreditNoProductSelected.insertCoin(tenCents);

        Assert.assertEquals(previousStackCreditSize + 1, hasCreditNoProductSelected.vendingMachine.getCreditStackSize());
        Assert.assertEquals(tenCents.denomination + previousCredit, hasCreditNoProductSelected.vendingMachine.getCredit(), ACCURACY);
        Assert.assertTrue(hasCreditNoProductSelected.vendingMachine.getCurrentState() instanceof HasCreditNoProductSelectedState);
    }

    @Test
    public void should_skip_insert_coins_after_dispensers_capacity_reached() {
        Coin tenCents = Coin.TEN_CENTS;
        int previousStackCreditSize = hasCreditNoProductSelected.vendingMachine.getCreditStackSize();
        double previousCredit = hasCreditNoProductSelected.vendingMachine.getCredit();
        int insertsToFillCoinShelf = 5;

        //fill ten cents dispenser shelf
        for (int i = 0; i < insertsToFillCoinShelf; i++) {
            hasCreditNoProductSelected.insertCoin(tenCents);
        }

        Assert.assertEquals(previousStackCreditSize + insertsToFillCoinShelf, hasCreditNoProductSelected.vendingMachine.getCreditStackSize());
        Assert.assertEquals(tenCents.denomination * insertsToFillCoinShelf + previousCredit, hasCreditNoProductSelected.vendingMachine.getCredit(), ACCURACY);
        Assert.assertTrue(hasCreditNoProductSelected.vendingMachine.getCurrentState() instanceof HasCreditNoProductSelectedState);

        //insert coins will no take any effect
        hasCreditNoProductSelected.insertCoin(tenCents);
        hasCreditNoProductSelected.insertCoin(tenCents);
        hasCreditNoProductSelected.insertCoin(tenCents);
        hasCreditNoProductSelected.insertCoin(tenCents);
        hasCreditNoProductSelected.insertCoin(tenCents);

        Assert.assertEquals(previousStackCreditSize + insertsToFillCoinShelf, hasCreditNoProductSelected.vendingMachine.getCreditStackSize());
        Assert.assertEquals(tenCents.denomination * insertsToFillCoinShelf + previousCredit, hasCreditNoProductSelected.vendingMachine.getCredit(), ACCURACY);
        Assert.assertTrue(hasCreditNoProductSelected.vendingMachine.getCurrentState() instanceof HasCreditNoProductSelectedState);

    }

    @Test
    public void should_select_valid_shelfNumber_and_change_state_to_HasCreditProductSelectedState() {
        hasCreditNoProductSelected.selectShelfNumber(0);
        Assert.assertNotNull(hasCreditNoProductSelected.vendingMachine.getSelectedProduct());
        Assert.assertTrue(hasCreditNoProductSelected.vendingMachine.getCurrentState() instanceof HasCreditProductSelectedState);
    }

    @Test
    public void should_select_invalid_shelfNumber_and_remain_state_to_HasCreditNoProductSelectedState() {
        hasCreditNoProductSelected.selectShelfNumber(5454);
        Assert.assertNull(hasCreditNoProductSelected.vendingMachine.getSelectedProduct());
        Assert.assertTrue(hasCreditNoProductSelected.vendingMachine.getCurrentState() instanceof HasCreditNoProductSelectedState);
    }

    @Test
    public void should_not_add_credit_return_all_inserted_credit_after_cancel_and_change_to_noCreditState() {
        hasCreditNoProductSelected.cancel();

        Assert.assertTrue(hasCreditNoProductSelected.vendingMachine.isCreditStackEmpty());
        Assert.assertEquals(0, hasCreditNoProductSelected.vendingMachine.getCredit(), ACCURACY);
        Assert.assertTrue(hasCreditNoProductSelected.vendingMachine.getCurrentState() instanceof NoCreditNoProductSelectedState);
    }

    @Test
    public void should_add_credit_return_all_inserted_credit_after_cancel_and_change_to_noCreditState() {
        List<Coin> coinsToInsert = Arrays.asList(Coin.FIFTY_CENTS, Coin.TEN_CENTS, Coin.TWENTY_CENTS, Coin.TWO, Coin.ONE, Coin.FIVE);

        int previousStackCreditSize = hasCreditNoProductSelected.vendingMachine.getCreditStackSize();
        double previousCredit = hasCreditNoProductSelected.vendingMachine.getCredit();

        coinsToInsert.forEach(hasCreditNoProductSelected::insertCoin);

        Assert.assertEquals(previousStackCreditSize + coinsToInsert.size(), hasCreditNoProductSelected.vendingMachine.getCreditStackSize());
        Double total = coinsToInsert.stream()
            .mapToDouble(coin -> coin.denomination)
            .reduce(Constants.SUM_DOUBLE_IDENTITY, Constants.SUM_DOUBLE_BINARY_OPERATOR);
        Assert.assertEquals(total + previousCredit, hasCreditNoProductSelected.vendingMachine.getCredit(), ACCURACY);

        hasCreditNoProductSelected.cancel();

        Assert.assertTrue(hasCreditNoProductSelected.vendingMachine.isCreditStackEmpty());
        Assert.assertEquals(0, hasCreditNoProductSelected.vendingMachine.getCredit(), ACCURACY);
        Assert.assertTrue(hasCreditNoProductSelected.vendingMachine.getCurrentState() instanceof NoCreditNoProductSelectedState);
    }
}
