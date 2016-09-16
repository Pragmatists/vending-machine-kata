package tdd.vendingMachine.strategy;

import tdd.vendingMachine.VendingMachineState;

import java.util.HashMap;

public class VendingMachineStateStrategies extends HashMap<VendingMachineState, IVendingMachineStrategy> {

    public VendingMachineStateStrategies() {
        put(VendingMachineState.WAITING_FOR_SELECT_PRODUCT, new WaitingForSelectProductStrategy());
        put(VendingMachineState.PRODUCT_SELECTED, new ProductSelectedStrategy());
        put(VendingMachineState.SET_UP_MACHINE, new SetUpVendingMachineStrategy());
    }
}
