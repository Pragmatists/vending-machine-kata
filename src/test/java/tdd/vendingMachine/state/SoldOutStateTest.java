package tdd.vendingMachine.state;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import tdd.vendingMachine.VendingMachine;
import tdd.vendingMachine.domain.Coin;
import tdd.vendingMachine.util.Constants;

import java.util.HashMap;

/**
 * @author Agustin Cabra on 2/20/2017.
 * @since 1.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({VendingMachine.class, SoldOutState.class})
@PowerMockIgnore(value = {"javax.management.*"})
public class SoldOutStateTest {

    private VendingMachine soldOutVM;

    @Before
    public void setup() {
        soldOutVM = Mockito.mock(VendingMachine.class);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void should_have_no_credit_after_inserting_coin_on_sold_out_machine() {

        soldOutVM.insertCoin(Coin.FIFTY_CENTS);
        Assert.assertEquals(0.0, soldOutVM.getCredit(), Constants.ACCURACY);
    }
}
