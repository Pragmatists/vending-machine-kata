package tdd.vendingMachine.strategy;

import tdd.vendingMachine.VendingMachineState;

import java.util.HashMap;

public class VendingMachineStateStrategies extends HashMap<VendingMachineState, IVendingMachineStrategy> implements IVendingMachineStrategies {

    public VendingMachineStateStrategies() {
        put(VendingMachineState.WAITING_FOR_SELECT_PRODUCT, new WaitingForSelectProductStrategy());
        put(VendingMachineState.PRODUCT_SELECTED, new ProductSelectedStrategy());
        put(VendingMachineState.SET_UP_MACHINE, new SetUpVendingMachineStrategy());
    }

    @Override
    public IVendingMachineStrategy get(VendingMachineState state) {
        return super.get(state);
    }
}
