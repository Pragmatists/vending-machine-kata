package tdd.vendingmachine.domain;

import tdd.vendingmachine.domain.dto.VendingMachineDto;

public class VendingMachineFacade {

    private final VendingMachineRepository vendingMachineRepository;

    VendingMachineFacade(VendingMachineRepository vendingMachineRepository) {
        this.vendingMachineRepository = vendingMachineRepository;
    }

    public void createVendingMachine(VendingMachineDto vendingMachineDto) {
        VendingMachine vendingMachine = VendingMachine.create(vendingMachineDto);
        vendingMachineRepository.save(vendingMachine);
    }

    public void selectShelfNumber(String shelfNumber) {
        VendingMachine vendingMachine = vendingMachineRepository.get();
        vendingMachine.selectShelfNumber(new ShelfNumber(shelfNumber));
        vendingMachineRepository.save(vendingMachine);
    }

    public String readDisplay() {
        VendingMachine vendingMachine = vendingMachineRepository.get();
        return vendingMachine.showDisplay();
    }
}
