package tdd.vendingMachine;

import tdd.vendingMachine.model.Product;

/**
 * @author Yevhen Sukhomud
 */
public interface Inventory {

    Product get();

    void put(Product product);

    void clean();
}
