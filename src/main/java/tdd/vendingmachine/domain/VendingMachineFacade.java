package tdd.vendingmachine.domain;

import tdd.vendingmachine.domain.dto.CoinDto;
import tdd.vendingmachine.domain.dto.ProductDto;
import tdd.vendingmachine.domain.dto.VendingMachineDto;

import java.util.Collection;

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

    public void insertCoin(CoinDto coinDto) {
        VendingMachine vendingMachine = vendingMachineRepository.get();
        vendingMachine.insertCoin(coinDto);
        vendingMachineRepository.save(vendingMachine);
    }

    public Collection<CoinDto> takeChange() {
        VendingMachine vendingMachine = vendingMachineRepository.get();
        Collection<CoinDto> change = vendingMachine.returnChange();
        vendingMachineRepository.save(vendingMachine);
        return change;
    }

    public Collection<ProductDto> takeProducts() {
        VendingMachine vendingMachine = vendingMachineRepository.get();
        Collection<ProductDto> products = vendingMachine.giveProducts();
        vendingMachineRepository.save(vendingMachine);
        return products;
    }
}
