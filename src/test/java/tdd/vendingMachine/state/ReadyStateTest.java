package tdd.vendingMachine.state;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.VendingMachineFactory;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.util.TestUtils.TestUtils;
import tdd.vendingMachine.view.VendingMachineMessages;

/**
 * @author Agustin Cabra on 2/21/2017.
 * @since 1.0
 */
public class ReadyStateTest implements StateTest {


    private Product COLA_199_025;
    private Product CHOCOLATE_BAR;
    private ReadyState readyState;
    private int coinShelfCapacity;
    private int initialCoinsOnShelf;
    VendingMachineFactory vendingMachineFactory;

    @Override
    public ReadyState transformToInitialState(VendingMachine vendingMachine) {
        Assert.assertEquals(0, vendingMachine.getCredit()); //no credit
        Assert.assertNull(vendingMachine.getSelectedProduct()); //no product
        Assert.assertTrue(vendingMachine.getCurrentState() instanceof ReadyState);
        return (ReadyState) vendingMachine.getCurrentState();
    }

    @Before @Override
    public void setup(){
        COLA_199_025 = new Product(199, "COLA_199_025");
        CHOCOLATE_BAR = new Product(149, "CHOCOLATE_BAR");
        coinShelfCapacity = 10;
        initialCoinsOnShelf = 5;
        vendingMachineFactory = new VendingMachineFactory();
        VendingMachine vendingMachine = vendingMachineFactory.customVendingMachineForTesting(TestUtils.buildShelvesWithItems(COLA_199_025, 1),
            TestUtils.buildCoinDispenserWithGivenItemsPerShelf(vendingMachineFactory.getVendingMachineConfiguration(), initialCoinsOnShelf));
        readyState = transformToInitialState(vendingMachine);
    }

    @After @Override
    public void tearDown(){
        vendingMachineFactory = null;
        COLA_199_025 = null;
        CHOCOLATE_BAR = null;
        readyState = null;
        coinShelfCapacity = 0;
        initialCoinsOnShelf = 0;
    }

    @Test
    public void should_change_state_after_inserting_coin_on_machine_with_dispenser_available() {
        Coin tenCents = Coin.TEN_CENTS;

        readyState.insertCoin(tenCents);

        Assert.assertEquals(tenCents.denomination, readyState.vendingMachine.getCredit());
        Assert.assertTrue(readyState.vendingMachine.getCurrentState() instanceof CreditNotSelectedProductState);
    }

    @Test
    public void should_not_change_state_and_skip_coin_insertions_when_coin_dispenser_full() {
        Coin tenCents = Coin.TEN_CENTS;
        VendingMachine vendingMachine = vendingMachineFactory.customVendingMachineForTesting(TestUtils.buildShelvesWithItems(CHOCOLATE_BAR, 1),
            TestUtils.buildCoinDispenserWithGivenItemsPerShelf(vendingMachineFactory.getVendingMachineConfiguration(), coinShelfCapacity));
        readyState = transformToInitialState(vendingMachine);

        readyState.insertCoin(tenCents);

        Assert.assertEquals(0, readyState.vendingMachine.getCredit());
        Assert.assertEquals(0, readyState.vendingMachine.getCreditStackSize());
        Assert.assertTrue(readyState.vendingMachine.getCurrentState() instanceof ReadyState);
    }

    @Test
    public void should_change_state_after_selecting_valid_shelfNumber_and_display_pending_balance_for_item() {
        readyState.selectShelfNumber(0);
        Assert.assertTrue(readyState.vendingMachine.getDisplayCurrentMessage()
            .contains(VendingMachineMessages.PENDING.label));
        Assert.assertNotNull(readyState.vendingMachine.getSelectedProduct());
        Assert.assertTrue(readyState.vendingMachine.getCurrentState() instanceof NoCreditSelectedProductState);
    }

    @Test
    public void should_not_change_state_after_selecting_invalid_shelfNumber() {
        readyState.selectShelfNumber(5515);
        transformToInitialState(readyState.vendingMachine);
    }

    @Test
    public void should_not_change_state_after_cancel() {
        readyState.cancel();
        transformToInitialState(readyState.vendingMachine);
    }
}
