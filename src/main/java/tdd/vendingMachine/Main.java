package tdd.vendingMachine;

import tdd.vendingMachine.state.VendingMachineFactory;

/**
 * @author Agustin on 2/26/2017.
 * @since 2.0
 */
public class Main {

    public static void main (String... args) {
        VendingMachine vendingMachine = new VendingMachineFactory().buildVendingMachineFromResourceFiles();
    }
}
