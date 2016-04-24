package tdd.vendingMachine.Config;

import tdd.vendingMachine.Domain.CoinRepo;
import tdd.vendingMachine.Domain.ProductRepo;
import tdd.vendingMachine.Domain.StorageRepo;
import tdd.vendingMachine.Service.CoinChanger;

/**
 * MachineControllerImpl can be constructed from instances of this class.
 */
public interface MachineConfig {
    ProductRepo getProductRepo();
    StorageRepo getStorageRepo();
    CoinRepo getCoinRepo();
    CoinChanger getCoinChanger();
}
