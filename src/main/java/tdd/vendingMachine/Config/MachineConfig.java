package tdd.vendingMachine.Config;

import tdd.vendingMachine.Domain.CoinRepo;
import tdd.vendingMachine.Domain.ProductRepo;
import tdd.vendingMachine.Domain.StorageRepo;

/**
 * Created by pm on 2/17/16.
 */
public interface MachineConfig {
    ProductRepo getProductRepo();
    StorageRepo getStorageRepo();
    CoinRepo getCoinRepo();
}
