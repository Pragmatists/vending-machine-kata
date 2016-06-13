package tdd.vendingMachine;

import org.junit.Test;
import tdd.vendingMachine.impl.BasicDisplay;

public class DisplayTest {

    @Test(expected = IllegalArgumentException.class)
    public void throws_exception_if_created_without_observer() {
        new BasicDisplay(null);
    }
}
