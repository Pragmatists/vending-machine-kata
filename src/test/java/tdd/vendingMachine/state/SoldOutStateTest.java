package tdd.vendingMachine.state;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.VendingMachineFactory;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.domain.Product;
import tdd.vendingMachine.util.TestUtils.TestUtils;
import tdd.vendingMachine.view.VendingMachineMessages;

import java.util.Collections;

/**
 * @author Agustin Cabra on 2/20/2017.
 * @since 1.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({VendingMachine.class, SoldOutState.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class SoldOutStateTest implements StateTest {

    private SoldOutState soldOutState;
    private VendingMachineFactory vendingMachineFactory;

    @Override
    public SoldOutState transformToInitialState(VendingMachine vendingMachine) {
        Assert.assertEquals(0, vendingMachine.getCredit()); //no credit
        Assert.assertNull(vendingMachine.getSelectedProduct()); //no product
        Assert.assertTrue(vendingMachine.getCurrentState() instanceof SoldOutState);
        return (SoldOutState) vendingMachine.getCurrentState();
    }

    @Before @Override
    public void setup() {
        vendingMachineFactory = new VendingMachineFactory();
        soldOutState = transformToInitialState(vendingMachineFactory.buildSoldOutVendingMachineNoCash(new Product(100, "COLA_025cl")));
    }

    @After @Override
    public void tearDown() {
        soldOutState = null;
        vendingMachineFactory = null;
    }

    @Test
    public void should_remain_sold_out_after_inserting_coin_and_return_coin_to_pickup_shelf() {
        soldOutState.insertCoin(Coin.FIFTY_CENTS);

        Assert.assertEquals(0, soldOutState.vendingMachine.getCredit());
        Assert.assertTrue(soldOutState.vendingMachine.getCurrentState() instanceof SoldOutState);
    }

    @Test
    public void should_remain_sold_out_and_display_product_price_after_valid_selecting_valid_shelfNumber() {
        soldOutState.selectShelfNumber(0);

        Assert.assertTrue(soldOutState.vendingMachine.getDisplayCurrentMessage()
            .contains(VendingMachineMessages.PRICE.label));//TODO: view todo list on googlespreadsheets
        Assert.assertEquals(0, soldOutState.vendingMachine.getCredit());
        Assert.assertTrue(soldOutState.vendingMachine.getCurrentState() instanceof SoldOutState);
    }

    @Test
    public void should_remain_sold_out_and_display_warning_after_selecting_invalid_shelfNumber() {
        soldOutState.selectShelfNumber(585);

        Assert.assertTrue(soldOutState.vendingMachine.getDisplayCurrentMessage()
            .contains(VendingMachineMessages.SHELF_NUMBER_NOT_AVAILABLE.label));//TODO: view todo list on googlespreadsheets
        Assert.assertEquals(0, soldOutState.vendingMachine.getCredit());
        Assert.assertTrue(soldOutState.vendingMachine.getCurrentState() instanceof SoldOutState);
    }

    @Test
    public void should_perform_no_actions_on_cancel_operation() {
        soldOutState.cancel();
        Assert.assertEquals(0, soldOutState.vendingMachine.getCredit());
        Assert.assertTrue(soldOutState.vendingMachine.getCurrentState() instanceof SoldOutState);
    }
}
