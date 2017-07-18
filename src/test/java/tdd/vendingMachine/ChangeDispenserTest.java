package tdd.vendingMachine;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.dto.Coin;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;

public class ChangeDispenserTest {

    private ChangeDispenser changeDispenser;

    @Before
    public void setup() {
        Injector injector = Guice.createInjector(new VendingMachineInjector());
        this.changeDispenser = injector.getInstance(ChangeDispenser.class);
    }

    @Test
    public void emptyDispenserTest() {
        assertThat(this.changeDispenser.retrieveCoins(), is(empty()));
    }

    @Test
    public void singleChangeDispenserTest() {
        Coin change = Coin.FIVE;
        this.changeDispenser.ejectChange(change);
        assertThat(this.changeDispenser.retrieveCoins(), contains(change));
        assertThat(this.changeDispenser.retrieveCoins(), is(empty()));
    }

    @Test
    public void multipleChangeDispenserTest() {
        Coin change1 = Coin.FIVE;
        Coin change2 = Coin.ONE;
        this.changeDispenser.ejectChange(change1);
        this.changeDispenser.ejectChange(change2);
        assertThat(this.changeDispenser.retrieveCoins(), contains(change1, change2));
        assertThat(this.changeDispenser.retrieveCoins(), is(empty()));
    }
}
