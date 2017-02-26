package tdd.vendingMachine.state;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.VendingMachine;

/**
 * @author Agustin on 2/26/2017.
 * @since 1.0
 */
public class TechnicalErrorStateTest implements StateTest {

    @Override
    public State transformToAndValidateInitialState(VendingMachine vendingMachine) {
        return null;
    }

    @Before @Override
    public void setup() {

    }

    @After @Override
    public void tearDown() {

    }

    @Test
    public void test() {
        Assert.fail();
    }
}
