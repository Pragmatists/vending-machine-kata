package tdd.vendingMachine.Service;

/**
 * The main implementation of this vending machine.
 * Allows Client- and Admin- operations.
 */
public class MainControllerImpl implements MachineClientController, MachineAdminController {
    public Response insertCoin(int nominal, int count) {
        return null;
    }

    public Response addCoins(int nominal, int count) {
        return null;
    }

    public Response setShelfProducts(int productid, int count, int shelfPosition) {
        return null;
    }
}
