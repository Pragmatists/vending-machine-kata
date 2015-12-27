package tdd.vendingMachine;

public final class VendingMachineStateFactory {

    public static MachineReadyInitialState machineReadyInitialState(VendingMachine vendingMachine) {
        return new MachineReadyInitialState(vendingMachine);
    }

    public static ProductSelectedState productSelectedState(VendingMachine vendingMachine) {
        return new ProductSelectedState(vendingMachine);
    }

    public static NotEnoughMoneyToChangeState notEnoughMoneyToChangeState(VendingMachine vendingMachine) {
        return new NotEnoughMoneyToChangeState(vendingMachine);
    }

    public static DispenseProductWithChangeState dispenseProductWithChangeState(VendingMachine vendingMachine) {
        return new DispenseProductWithChangeState(vendingMachine);
    }

    private VendingMachineStateFactory() {

    }
}
