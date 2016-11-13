package tdd.vendingMachine.service;

import tdd.vendingMachine.model.Product;
import tdd.vendingMachine.model.VendingMachine;
import tdd.vendingMachine.service.exception.InvalidShelfException;

public interface IVendingMachineStateService extends Cancelable, Confirmable {
    void configurVendingMachine(VendingMachine vendingMachine);

    Product getSelectedShelfProduct();

    void selectShelf(Integer shelfNo) throws InvalidShelfException;

    Product prepareReleaseSelectedProduct();
}
