package tdd.vendingMachine.state;

import tdd.vendingMachine.VendingMachine;

/**
 * @author Mateusz Urbański <matek2305@gmail.com>
 */
public interface VendingMachineState {

    void proceed(VendingMachine vendingMachine);
}
