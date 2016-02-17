package tdd.vendingMachine.Config;

import lombok.Data;
import tdd.vendingMachine.Domain.CoinRepo;
import tdd.vendingMachine.Domain.Product;
import tdd.vendingMachine.Domain.ProductRepo;
import tdd.vendingMachine.Domain.StorageRepo;

import java.util.Arrays;

/**
 * Vending machine filled with some content for system testing.
 */

@Data
public class MockMachineConfig1 implements MachineConfig {
    ProductRepo productRepo;
    StorageRepo storageRepo;
    CoinRepo coinRepo;

    public MockMachineConfig1() {
        productRepo = new ProductRepo();
        productRepo.save(new Product("Sprite", 100));
        productRepo.save(new Product("Coca-Cola", 100));
        productRepo.save(new Product("Brand new crown cork", 10));
        productRepo.save(new Product("Super-durable plastic bottle", 20));
        storageRepo = new StorageRepo(5, 5);
        storageRepo.setProductAtShelf(0, 0, 5);
        storageRepo.setProductAtShelf(1, 0, 1);
        storageRepo.setProductAtShelf(2, 1, 5);
        storageRepo.setProductAtShelf(3, 2, 5);
        storageRepo.setProductAtShelf(4, 3, 1);

        coinRepo = new CoinRepo(Arrays.asList(new Integer[]{10, 20, 50, 100, 200, 500}));
        coinRepo.addCoins(10, 5);
        coinRepo.addCoins(50, 2);
        coinRepo.addCoins(200, 2);
        coinRepo.addCoins(500, 2);
    }
}
