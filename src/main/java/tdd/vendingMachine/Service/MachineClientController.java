package tdd.vendingMachine.Service;

/**
 * Service of operations allowed for the client of the machine.
 */
public interface MachineClientController {
    Response insertCoin(int nominal, int count);
}
