package tdd.vendingMachine.strategy;

import tdd.vendingMachine.VendingMachineState;

public interface IVendingMachineStrategies {
    IVendingMachineStrategy get(VendingMachineState state);
}
