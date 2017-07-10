package tdd.vendingmachine.domain;

import tdd.vendingmachine.domain.exception.VendingMachineNotFound;

class VendingMachineInMemoryRepository implements VendingMachineRepository {

    private VendingMachine vendingMachine = null;

    @Override
    public void save(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    @Override
    public VendingMachine get() {
        if (vendingMachine == null) {
            throw new VendingMachineNotFound();
        }
        return vendingMachine;
    }
}
