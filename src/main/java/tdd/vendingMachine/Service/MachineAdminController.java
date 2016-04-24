package tdd.vendingMachine.Service;

/**
 * Service of operations allowed for the admin of the machine only.
 */
public interface MachineAdminController {
    Response addCoins(int nominal, int count);
    Response setShelfProducts(int productid, int count, int shelfPosition);
}
