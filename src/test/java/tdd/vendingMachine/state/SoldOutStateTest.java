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

    private SoldOutState soldOutState;

    @Before
    public void setup() {
        VendingMachine vendingMachine = new VendingMachine(Collections.emptyMap(), Collections.emptyMap());

        //validate initial state
        Assert.assertEquals(0, vendingMachine.getCredit(), ACCURACY); //no credit
        Assert.assertNull(vendingMachine.getSelectedProduct()); //no product
        Assert.assertTrue(vendingMachine.getCurrentState() instanceof SoldOutState);
        soldOutState = (SoldOutState) vendingMachine.getCurrentState();
    }

    @After
    public void tearDown() {
        soldOutState = null;
    }

    @Test
    public void should_have_no_credit_after_inserting_coin_on_sold_out_machine() {
        soldOutState.insertCoin(Coin.FIFTY_CENTS);
        Assert.assertEquals(0.0, soldOutState.vendingMachine.getCredit(), Constants.ACCURACY);
    }

    @Test
    public void should_on_sold_out_state_after_inserting_coin_credit_should_be_zero() {
        soldOutState.insertCoin(Coin.FIFTY_CENTS);

        Assert.assertTrue(soldOutState.vendingMachine.getCurrentState() instanceof SoldOutState);
        Assert.assertEquals(0, soldOutState.vendingMachine.getCredit(), ACCURACY);
    }
}
