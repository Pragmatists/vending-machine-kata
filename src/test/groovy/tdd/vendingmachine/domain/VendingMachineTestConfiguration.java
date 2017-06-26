package tdd.vendingmachine.domain;

public class VendingMachineTestConfiguration {

    public static VendingMachineFacade vendingMachineFacade() {
        VendingMachineRepository vendingMachineRepository = new VendingMachineInMemoryRepository();
        return new VendingMachineFacade(vendingMachineRepository);
    }
}
