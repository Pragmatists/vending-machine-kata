package tdd.vendingmachine.domain;

interface VendingMachineRepository {

    void save(VendingMachine vendingMachine);

    VendingMachine get();
}
