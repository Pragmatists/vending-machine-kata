package tdd.vendingMachine.state;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.VendingMachineFactory;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.util.Constants;
import tdd.vendingMachine.util.TestUtils.TestUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 */
public class CreditNotSelectedProductStateTest implements StateTest {

    private Product COLA_199_025;
    private Product CHIPS_025;
    private Product CHOCOLATE_BAR;
    CreditNotSelectedProductState creditNotSelectedProductState;
    VendingMachineFactory vendingMachineFactory;


    @Override
    public CreditNotSelectedProductState transformToInitialState(VendingMachine vendingMachine) {
        Assert.assertEquals(0, vendingMachine.getCredit()); //no credit
        Assert.assertNull(vendingMachine.getSelectedProduct()); //no product
        Assert.assertTrue(vendingMachine.getCurrentState() instanceof ReadyState);
        ReadyState initialState = (ReadyState) vendingMachine.getCurrentState();

        //transform to desired state
        initialState.insertCoin(Coin.FIFTY_CENTS);

        //validate initial state
        Assert.assertEquals(Coin.FIFTY_CENTS.denomination, initialState.vendingMachine.getCredit());
        Assert.assertTrue(initialState.vendingMachine.getCurrentState() instanceof CreditNotSelectedProductState);
        return (CreditNotSelectedProductState) initialState.vendingMachine.getCurrentState();
    }

    @Before @Override
    public void setup(){
        COLA_199_025 = new Product(199, "COLA_199_025");
        CHIPS_025 = new Product(129, "CHIPS_025");
        CHOCOLATE_BAR = new Product(149, "CHOCOLATE_BAR");
        vendingMachineFactory = new VendingMachineFactory();
        VendingMachine vendingMachine = vendingMachineFactory.customVendingMachineForTesting(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildCoinDispenserWithGivenItemsPerShelf(vendingMachineFactory.getVendingMachineConfiguration(), 5));

        creditNotSelectedProductState = transformToInitialState(vendingMachine);
    }

    @After @Override
    public void tearDown(){
        COLA_199_025 = null;
        CHIPS_025 = null;
        CHOCOLATE_BAR = null;
        creditNotSelectedProductState = null;
        vendingMachineFactory = null;
    }

    @Test
    public void should_insert_credit_remain_same_state() {
        Coin tenCents = Coin.TEN_CENTS;
        int previousStackCreditSize = creditNotSelectedProductState.vendingMachine.getCreditStackSize();
        int previousCredit = creditNotSelectedProductState.vendingMachine.getCredit();

        creditNotSelectedProductState.insertCoin(tenCents);

        Assert.assertEquals(previousStackCreditSize + 1, creditNotSelectedProductState.vendingMachine.getCreditStackSize());
        Assert.assertEquals(tenCents.denomination + previousCredit, creditNotSelectedProductState.vendingMachine.getCredit());
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCurrentState() instanceof CreditNotSelectedProductState);
    }

    @Test
    public void should_skip_insert_coins_after_dispensers_capacity_reached() {
        Coin tenCents = Coin.TEN_CENTS;
        int previousStackCreditSize = creditNotSelectedProductState.vendingMachine.getCreditStackSize();
        int previousCredit = creditNotSelectedProductState.vendingMachine.getCredit();
        int insertsToFillCoinShelf = 5;

        //fill ten cents dispenser shelf
        for (int i = 0; i < insertsToFillCoinShelf; i++) {
            creditNotSelectedProductState.insertCoin(tenCents);
        }

        Assert.assertEquals(previousStackCreditSize + insertsToFillCoinShelf, creditNotSelectedProductState.vendingMachine.getCreditStackSize());
        Assert.assertEquals(tenCents.denomination * insertsToFillCoinShelf + previousCredit, creditNotSelectedProductState.vendingMachine.getCredit());
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCurrentState() instanceof CreditNotSelectedProductState);

        //insert coins will no take any effect
        creditNotSelectedProductState.insertCoin(tenCents);
        creditNotSelectedProductState.insertCoin(tenCents);
        creditNotSelectedProductState.insertCoin(tenCents);
        creditNotSelectedProductState.insertCoin(tenCents);
        creditNotSelectedProductState.insertCoin(tenCents);

        Assert.assertEquals(previousStackCreditSize + insertsToFillCoinShelf, creditNotSelectedProductState.vendingMachine.getCreditStackSize());
        Assert.assertEquals(tenCents.denomination * insertsToFillCoinShelf + previousCredit, creditNotSelectedProductState.vendingMachine.getCredit());
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCurrentState() instanceof CreditNotSelectedProductState);

    }

    @Test
    public void should_select_valid_shelfNumber_and_change_state_to_HasCreditProductSelectedState() {
        creditNotSelectedProductState.selectShelfNumber(0);
        Assert.assertNotNull(creditNotSelectedProductState.vendingMachine.getSelectedProduct());
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCurrentState() instanceof InsufficientCreditState);
    }

    @Test
    public void should_select_invalid_shelfNumber_and_remain_state_to_HasCreditNoProductSelectedState() {
        creditNotSelectedProductState.selectShelfNumber(5454);
        Assert.assertNull(creditNotSelectedProductState.vendingMachine.getSelectedProduct());
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCurrentState() instanceof CreditNotSelectedProductState);
    }

    @Test
    public void should_not_add_credit_return_all_inserted_credit_after_cancel_and_change_to_noCreditState() {
        creditNotSelectedProductState.cancel();

        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.isCreditStackEmpty());
        Assert.assertEquals(0, creditNotSelectedProductState.vendingMachine.getCredit());
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCurrentState() instanceof ReadyState);
    }

    @Test
    public void should_add_credit_return_all_inserted_credit_after_cancel_and_change_to_noCreditState() {
        List<Coin> coinsToInsert = Arrays.asList(Coin.FIFTY_CENTS, Coin.TEN_CENTS, Coin.TWENTY_CENTS, Coin.TWO, Coin.ONE, Coin.FIVE);

        int previousStackCreditSize = creditNotSelectedProductState.vendingMachine.getCreditStackSize();
        int previousCredit = creditNotSelectedProductState.vendingMachine.getCredit();

        coinsToInsert.forEach(creditNotSelectedProductState::insertCoin);

        Assert.assertEquals(previousStackCreditSize + coinsToInsert.size(), creditNotSelectedProductState.vendingMachine.getCreditStackSize());
        int total = coinsToInsert.stream()
            .mapToInt(coin -> coin.denomination)
            .reduce(Constants.SUM_INT_IDENTITY, Constants.SUM_INT_BINARY_OPERATOR);
        Assert.assertEquals(total + previousCredit, creditNotSelectedProductState.vendingMachine.getCredit());

        creditNotSelectedProductState.cancel();

        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.isCreditStackEmpty());
        Assert.assertEquals(0, creditNotSelectedProductState.vendingMachine.getCredit());
        Assert.assertTrue(creditNotSelectedProductState.vendingMachine.getCurrentState() instanceof ReadyState);
    }
}
