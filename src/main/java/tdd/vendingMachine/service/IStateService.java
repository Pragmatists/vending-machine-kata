package tdd.vendingMachine.service;

import tdd.vendingMachine.model.Product;
import tdd.vendingMachine.model.VendingMachine;
import tdd.vendingMachine.service.exception.InvalidShelfException;

public interface IStateService {
    void configurVendingMachine(VendingMachine vendingMachine);

    Product getProductOnShelf(Integer shelfNo) throws InvalidShelfException;
}
