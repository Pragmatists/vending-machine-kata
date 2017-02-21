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
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.util.Constants;

import java.util.Collections;

import static tdd.vendingMachine.util.Constants.ACCURACY;

/**
 * @author Agustin Cabra on 2/20/2017.
 * @since 1.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({VendingMachine.class, SoldOutState.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class SoldOutStateTest {

    private VendingMachine emptyVendingMachine;

    @Before
    public void setup() {
        emptyVendingMachine = new VendingMachine(Collections.emptyMap(), Collections.emptyMap());
    }

    @After
    public void tearDown() {
        emptyVendingMachine = null;
    }

    @Test
    public void should_have_no_credit_after_inserting_coin_on_sold_out_machine() {
        emptyVendingMachine.insertCoin(Coin.FIFTY_CENTS);
        Assert.assertEquals(0.0, emptyVendingMachine.getCredit(), Constants.ACCURACY);
    }

    @Test
    public void should_be_sold_out_state_for_empty_created_vending_machine() {
        Assert.assertTrue(emptyVendingMachine.getCurrentState() instanceof SoldOutState);
    }

    @Test
    public void should_on_sold_out_state_after_inserting_coin_credit_should_be_zero() {
        Assert.assertTrue(emptyVendingMachine.getCurrentState() instanceof SoldOutState);

        emptyVendingMachine.insertCoin(Coin.FIFTY_CENTS);

        Assert.assertTrue(emptyVendingMachine.getCurrentState() instanceof SoldOutState);
        Assert.assertEquals(0, emptyVendingMachine.getCredit(), ACCURACY);
    }
}
