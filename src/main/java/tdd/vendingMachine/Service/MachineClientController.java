package tdd.vendingMachine.Service;

/**
 * Service of operations allowed for the client of the machine.
 */
public interface MachineClientController {
    Response getShelfContents();
    Response selectShelf(int shelf);
    Response insertCoin(int nominal);
    Response cancelTransaction();
}
