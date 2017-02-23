package tdd.vendingMachine.state;

import org.junit.After;
import org.junit.Before;
import tdd.vendingMachine.VendingMachine;

/**
 * @author Agustin Cabra on 2/23/2017.
 * @since 1.0
 * This interface should be implemented on Tests for STATE Class objects, provides a useful
 * method to transform a given vending machine from the given state to the desired initial
 * state of the test.
 */
public interface StateTest<T extends State> {

    /**
     * Validate and provides the initial desired state
     * @param vendingMachine the vendingMachine to set to initial state upon actions
     * @return the state of the given vendingMachine after actions
     */
    T transformToInitialState(VendingMachine vendingMachine);

    /**
     * Method meant to be annotated with @Before, to declare initial variables required for tests
     */
    void setup();

    /**
     * Method meant to be annotated with @After, to clear the variables after test performed
     */
    void tearDown();
}
